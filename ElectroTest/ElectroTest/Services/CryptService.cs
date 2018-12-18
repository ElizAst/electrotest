using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace ElectroTest.Services
{
    /// <summary>
    /// Crypt Service
    /// </summary>
    public class CryptService
    {
        /// <summary>
        /// Get Hash MD5
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public static string GetMd5Hash(string input)
        {
            StringBuilder sBuilder = new StringBuilder();
            using (MD5 md5Hash = MD5.Create())
            {
                byte[] data = md5Hash.ComputeHash(Encoding.UTF8.GetBytes(input));
                for (int i = 0; i < data.Length; i++)
                    sBuilder.Append(data[i].ToString("x2"));
            }
            return sBuilder.ToString();
        }

        /// <summary>
        /// Compare Input and Hash
        /// </summary>
        /// <param name="input"></param>
        /// <param name="hash"></param>
        /// <returns></returns>
        public static bool VerifyHash(string input, string hash)
        {
            string inputHash = GetMd5Hash(input);
            return inputHash == hash;
        }
    }
}
