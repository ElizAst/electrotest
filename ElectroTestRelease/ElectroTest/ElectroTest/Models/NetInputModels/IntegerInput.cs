using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ElectroTest.Models.NetInputModels
{
    public class IntegerInput
    {
        public int Value { get; set; }
    }

    public class IsStartTest
    {
        public bool IsStart { get; set; }
        public int TestID { get; set; }
        public int UserID { get; set; }
    }
}
