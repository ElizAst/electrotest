using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ElectroTestWeb.Models
{
    public class UserAnswer
    {
        public int ID { get; set; }

        public int TestResultID { get; set; }
        public TestResult TestResult { get; set; }

        public int QuestionID { get; set; }
        public Question Question { get; set; }

        public int AnswerID { get; set; }
        public Answer Answer { get; set; }
    }
}
