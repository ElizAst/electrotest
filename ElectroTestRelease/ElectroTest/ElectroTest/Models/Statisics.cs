using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ElectroTestWeb.Models
{
    public class Statisics
    {
        public int ID { get; set; }

        public int UserID { get; set; }
        public User User { get; set; }

        public int TestResultID { get; set; }
        public TestResult TestResult { get; set; }
    }
}
