using ElectroTest.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ElectroTestWeb.Models
{
    public class Question
    {
        public int ID { get; set; }
        public string QuestionContent { get; set; }
        public int TicketNum { get; set; }

        public List<Answer> Answers { get; set; }
        public List<UserAnswer> UserAnswers { get; set; }
        public List<RightQuestions> RightQuestions { get; set; }
    }

    public class Answer
    {
        public int ID { get; set; }
        public string AnswerContent { get; set; }

        public int QuestionID { get; set; }
        public Question Question { get; set; }

        public List<UserAnswer> UserAnswers { get; set; }
        public List<RightQuestions> RightQuestions { get; set; }
    }

    public class RightQuestions
    {
        public int ID { get; set; }
        public int AnswerID { get; set; }
        public int QuestionID { get; set; }

        public Answer Answer { get; set; }
        public Question Question { get; set; }
    }
}
