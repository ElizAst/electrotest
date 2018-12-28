using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;
using ElectroTest.Models.NetInputModels;
using ElectroTest.Models.NetModels;
using ElectroTest.Services;
using ElectroTestWeb.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace ElectroTest.Controllers
{
    public class HomeController : Controller
    {
        /// <summary>
        /// Получить в представлении IActionResultNet вопрос с ответами
        /// </summary>
        /// <param name="question"></param>
        /// <param name="mainDBContext"></param>
        /// <returns></returns>
        private QuestionActionNet GetQuestionActionNet(Question question, MainDBContext mainDBContext, List<UserAnswer> questions)
        {
            var userAnswer = questions.FirstOrDefault(x => x.QuestionID == question.ID);
            var right = mainDBContext.RightQuestions.FirstOrDefault(x => x.QuestionID == question.ID);
            return new QuestionActionNet
            {
                WorkID = question.ID,
                ResultCode = userAnswer == null ? ResultCode.Error : ResultCode.Success,
                QuestionContent = question.QuestionContent,
                AnswerActionNets = mainDBContext.Answers.Where(y => y.Question == question)
                    .Select(y => new AnswerActionNet
                    {
                        ResultCode = userAnswer != null && userAnswer.AnswerID == y.ID ? ResultCode.Success : ResultCode.Error,
                        WorkID = y.ID,
                        AnswerContent = y.AnswerContent,
                        IsRight = y.ID == right.AnswerID
                    }).ToList()
            };
        }

        /// <summary>
        /// Войти в систему
        /// </summary>
        /// <param name="userModel"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject Login([FromBody]UserModel userModel)
        {
            if (!ModelState.IsValid)
                return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet
                {
                    ResultCode = ResultCode.Error,
                    Message = string.Join("\n", ModelState.Values.Select(x => x.Errors).SelectMany(x => x).Select(x => x.ErrorMessage))
                }));
            MainDBContext mainDBContext = new MainDBContext();
            var user = mainDBContext.Users.FirstOrDefault(x => x.Nickname == userModel.Login);
            if (user != null)
                if (CryptService.GetMd5Hash(userModel.Password) == user.PasswordHash)
                    return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet
                    {
                        ResultCode = ResultCode.Success,
                        WorkID = user.ID,
                        IsTestWork = mainDBContext.Statisics.Where(x => x.User == user).Count() > 0
                    }));
            return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet { ResultCode = ResultCode.ErrorLoginPassword }));
        }

        /// <summary>
        /// Получить статистики
        /// </summary>
        /// <param name="user"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject GetStatistics([FromBody]IntegerInput user)
        {
            MainDBContext mainDBContext = new MainDBContext();
            var testResults = mainDBContext.Statisics.Where(x => x.UserID == user.Value).Select(x => x.TestResultID);
            var tmpTestRes = new List<int>();
            foreach (var testResult in testResults)
            {
                var ticket = mainDBContext.UserAnswers.Where(x => x.TestResultID == testResult)
                    .Select(x => x.Question.TicketNum).FirstOrDefault();
                var userCounts = mainDBContext.UserAnswers.Where(x => x.TestResultID == testResult).Count();
                if (mainDBContext.Questions.Where(x => x.TicketNum == ticket).Count() == userCounts)
                    tmpTestRes.Add(testResult);
            }
            testResults = tmpTestRes.AsQueryable();

            var statisticsList = mainDBContext.TestResults.Where(x => testResults.Contains(x.ID))
                .Select(x => new StatisticResult
                {
                    ResultCode = ResultCode.Success,
                    WorkID = x.ID,
                    DateTime = x.DateTime.ToLongDateString()
                }).ToList();
            for (int i = 0; i < statisticsList.Count; i++)
            {
                statisticsList[i].Ticket = mainDBContext.UserAnswers.Where(x => x.TestResultID == statisticsList[i].WorkID)
                    .Select(x => x.Question.TicketNum).FirstOrDefault();
                int ticketCount = mainDBContext.Questions.Where(x => x.TicketNum == statisticsList[i].Ticket).Count();
                var userAnswers = mainDBContext.UserAnswers.Where(x => x.TestResultID == statisticsList[i].WorkID);
                int usersRight = 0;
                foreach (var userAnswer in userAnswers)
                    if (mainDBContext.RightQuestions.FirstOrDefault(x => x.AnswerID == userAnswer.AnswerID
                                                                     && x.QuestionID == userAnswer.QuestionID) != null)
                        usersRight++;
                if (usersRight == 0)
                    statisticsList[i].Result = 0;
                else
                    statisticsList[i].Result = usersRight * 100 / ticketCount;
            }
            return JObject.Parse(JsonConvert.SerializeObject(new StatisticsList
            {
                ResultCode = statisticsList.Count() > 0 ? ResultCode.Success : ResultCode.Error,
                WorkID = user.Value,
                staticticResults = statisticsList.ToList()
            }));
        }

        /// <summary>
        /// Регистрация
        /// </summary>
        /// <param name="registerModel"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject Register([FromBody]RegisterModel registerModel)
        {
            if (!ModelState.IsValid)
                return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet
                {
                    ResultCode = ResultCode.Error,
                    Message = string.Join("\n", ModelState.Values.Select(x => x.Errors).SelectMany(x => x).Select(x => x.ErrorMessage))
                }));
            if (registerModel.Password != registerModel.PasswordConfirmed)
                return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet { ResultCode = ResultCode.ErrorLoginPassword }));
            MainDBContext mainDBContext = new MainDBContext();
            if (mainDBContext.Users.FirstOrDefault(x => x.Nickname == registerModel.Login) != null)
                return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet { ResultCode = ResultCode.ErrorLoginPassword, WorkID = 0 }));
            var user = new User
            {
                Nickname = registerModel.Login,
                PasswordHash = CryptService.GetMd5Hash(registerModel.Password),
            };
            mainDBContext.Entry(user).State = Microsoft.EntityFrameworkCore.EntityState.Added;
            mainDBContext.SaveChanges();
            return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet { ResultCode = ResultCode.Success, WorkID = user.ID }));
        }

        /// <summary>
        /// Начать тест
        /// </summary>
        /// <param name="user"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject StartTest([FromBody]IntegerInput user)
        {
            MainDBContext mainDBContext = new MainDBContext();
            var countOfQuestionInTicket = mainDBContext.Questions.GroupBy(x => x.TicketNum).Select(x => new { Ticket = x.Key, Count = x.Count() });
            var usersAnswerAll = mainDBContext.Statisics.Where(x => x.UserID == user.Value).Select(x => x.TestResult);
            foreach (var userAnswer in usersAnswerAll)
            {
                var count = mainDBContext.UserAnswers.Where(x => x.TestResultID == userAnswer.ID).Select(x => x.Question);
                if (count.Count() > 0)
                {
                    var type = count.First().TicketNum;
                    if (count.Count() < countOfQuestionInTicket.FirstOrDefault(x => x.Ticket == type).Count)
                        return JObject.Parse(JsonConvert.SerializeObject(new StartActionNet
                        {
                            IsStartPrev = true,
                            PrevID = userAnswer.ID,
                            ResultCode = ResultCode.Success,
                            WorkID = user.Value
                        }));
                }
                else
                {
                    return JObject.Parse(JsonConvert.SerializeObject(new StartActionNet
                    {
                        IsStartPrev = true,
                        PrevID = userAnswer.ID,
                        ResultCode = ResultCode.Success,
                        WorkID = user.Value
                    }));
                }
            }
            var testResult = new TestResult { DateTime = DateTime.Now };
            var statistics = new Statisics { TestResult = testResult, UserID = user.Value };
            mainDBContext.Entry(testResult).State = Microsoft.EntityFrameworkCore.EntityState.Added;
            mainDBContext.Entry(statistics).State = Microsoft.EntityFrameworkCore.EntityState.Added;
            mainDBContext.SaveChanges();
            return JObject.Parse(JsonConvert.SerializeObject(new StartActionNet { IsStartPrev = false, WorkID = user.Value, ResultCode = ResultCode.Success, PrevID = testResult.ID }));
        }

        /// <summary>
        /// Получить все id билетов
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        public JObject GetTickets()
        {
            MainDBContext mainDBContext = new MainDBContext();
            var questionNums = mainDBContext.Questions.GroupBy(x => x.TicketNum).Select(x => x.Key);
            return JObject.Parse(JsonConvert.SerializeObject(new TicketsNet
            {
                ResultCode = ResultCode.Success,
                Tickets = questionNums.ToList(),
                WorkID = 0
            }));
        }

        /// <summary>
        /// Завершить тест
        /// </summary>
        /// <param name="integer"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject FinishTest([FromBody]FinishTestInput integer)
        {
            MainDBContext mainDBContext = new MainDBContext();
            if (integer.Timeout)
            {
                mainDBContext.Remove(mainDBContext.TestResults.Find(integer.Value));
                mainDBContext.SaveChanges();
                return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet
                {
                    ResultCode = integer.Timeout ? ResultCode.Success : ResultCode.Error,
                    WorkID = integer.Value
                }));
            }
            var userAnswers = mainDBContext.UserAnswers.Where(x => x.TestResultID == integer.Value).Select(x => x.Question);
            bool isEnd = false;
            if (userAnswers.Count() > 0)
            {
                var type = userAnswers.First().TicketNum;
                isEnd = mainDBContext.Questions.Where(x => x.TicketNum == type).Count() == userAnswers.Count();
            }
            return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet
            {
                ResultCode = isEnd ? ResultCode.Success : ResultCode.Error,
                WorkID = integer.Value
            }));
        }

        /// <summary>
        /// Получить вопросы
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject GetQuestions([FromBody]PrestartInfo id)
        {
            MainDBContext mainDBContext = new MainDBContext();
            var questions = mainDBContext.UserAnswers.Where(x => x.TestResultID == id.UserID).ToList();
            var quesionsList = mainDBContext.Questions.Where(x => x.TicketNum == id.Ticket).ToList();

            return JObject.Parse(JsonConvert.SerializeObject(new QuestionListActionNet
            {
                ResultCode = quesionsList.Count > 0 ? ResultCode.Error : ResultCode.Success,
                WorkID = id.UserID,
                QuestionActionNets = quesionsList.Select(x => GetQuestionActionNet(x, mainDBContext, questions)).ToList()
            }));
        }

        /// <summary>
        /// Ответить на вопрос
        /// </summary>
        /// <param name="userAnswer"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject Answer([FromBody] UserAnswerInputNet userAnswer)
        {
            MainDBContext mainDBContext = new MainDBContext();
            var rightQuestion = mainDBContext.RightQuestions.FirstOrDefault(x => x.QuestionID == userAnswer.QuestionID);
            var isAnswered = mainDBContext.UserAnswers
                .FirstOrDefault(x => x.QuestionID == userAnswer.QuestionID
                && x.TestResultID == userAnswer.TestResult);

            if (isAnswered == null)
                mainDBContext.UserAnswers.Add(new UserAnswer
                {
                    AnswerID = userAnswer.AnswerID,
                    QuestionID = userAnswer.QuestionID,
                    TestResultID = userAnswer.TestResult
                });
            else
            {
                isAnswered.AnswerID = userAnswer.AnswerID;
                mainDBContext.Entry(isAnswered).State = Microsoft.EntityFrameworkCore.EntityState.Modified;
            }

            mainDBContext.SaveChanges();
            return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet
            {
                ResultCode = rightQuestion.AnswerID == userAnswer.AnswerID ? ResultCode.Right : ResultCode.Wrong,
                WorkID = userAnswer.UserID
            }));
        }

        /// <summary>
        /// Пользовательский ответ на предложение продолжить тест
        /// </summary>
        /// <param name="isStartTest"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject IsStartTest([FromBody]IsStartTest isStartTest)
        {
            MainDBContext mainDBContext = new MainDBContext();
            if (!isStartTest.IsStart)
            {
                var test = mainDBContext.TestResults.Find(isStartTest.TestID);
                mainDBContext.Statisics.Remove(mainDBContext.Statisics.FirstOrDefault(x => x.TestResult == test));
                mainDBContext.TestResults.Remove(test);
                mainDBContext.SaveChanges();
            }
            int resID = 0;
            if (isStartTest.IsStart)
                resID = isStartTest.TestID;
            else
                resID = JsonConvert.DeserializeObject<StartActionNet>(StartTest(
                    new IntegerInput
                    {
                        Value = isStartTest.UserID
                    }).ToString()).PrevID;
            return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet
            {
                ResultCode = isStartTest.IsStart ? ResultCode.Success : ResultCode.Error,
                WorkID = resID
            }));
        }


        /// <summary>
        /// Получить пользовательский профиль
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject ProfileUserInfo([FromBody] IntegerInput id)
        {
            MainDBContext mainDBContext = new MainDBContext();
            var user = mainDBContext.Users.Find(id.Value);
            return JObject.Parse(JsonConvert.SerializeObject(new UserProfileNet
            {
                Nickname = user.Nickname,
                ResultCode = ResultCode.Success,
                WorkID = user.ID
            }));
        }


        [HttpPost]
        public JObject SaveUser([FromBody]UserSave userSave)
        {
            MainDBContext mainDBContext = new MainDBContext();
            var user = mainDBContext.Users.Find(userSave.Id);
            user.Nickname = userSave.nickname;
            mainDBContext.Entry(user).State = Microsoft.EntityFrameworkCore.EntityState.Modified;
            mainDBContext.SaveChanges();
            return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet
            {
                ResultCode = ResultCode.Success
            }));
        }

    }
}
