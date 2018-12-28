using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ElectroTest.Models.NetInputModels
{
    public class UserAnswerInputNet
    {
        public int UserID { get; set; }
        public int QuestionID { get; set; }
        public int AnswerID { get; set; }
        public int TestResult { get; set; }
    }
}
