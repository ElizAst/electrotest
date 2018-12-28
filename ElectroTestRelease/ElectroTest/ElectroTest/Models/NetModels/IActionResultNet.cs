using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ElectroTest.Models.NetModels
{
    public enum ResultCode
    {
        Success,
        Error,
        ErrorLoginPassword,
        Wrong,
        Right
    }

    public enum Boolean
    {
        True,
        False
    }

    public interface IActionResultNet
    {
        ResultCode ResultCode { get; set; }
        int WorkID { get; set; }
    }

    public class UniversalActionNet : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public string Message { get; set; }
        public bool IsTestWork { get; set; }
    }

    public class StartActionNet : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public bool IsStartPrev { get; set; }
        public int PrevID { get; set; }
    }

    public class AnswerActionNet : IActionResultNet
    {
        public bool IsRight { get; set; }
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public string AnswerContent { get; set; }
    }

    public class QuestionActionNet : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public string QuestionContent { get; set; }
        public List<AnswerActionNet> AnswerActionNets { get; set; }
    }

    public class QuestionListActionNet : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public List<QuestionActionNet> QuestionActionNets { get; set; }
    }

    public class StatisticResult : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public int Result { get; set; }
        public string DateTime { get; set; }
        public int Ticket { get; set; }
    }

    public class StatisticsList : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public List<StatisticResult> staticticResults { get; set; }
    }

    public class UserProfileNet : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public string Nickname { get; set; }
    }

    public class TicketsNet : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public List<int> Tickets { get; set; }
    }
}
