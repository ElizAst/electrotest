using ElectroTest.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ElectroTestWeb.Models
{
    public class TestResult
    {
        public int ID { get; set; }

        public DateTime DateTime { get; set; }
        public float PercentResult { get; set; } = 0;

        public List<UserAnswer> UserAnswers { get; set; }
        public List<Statisics> Statisics { get; set; }
    }
}
