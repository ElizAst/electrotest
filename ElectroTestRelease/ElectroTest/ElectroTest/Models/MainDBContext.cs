using ElectroTest.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ElectroTestWeb.Models
{
    public class MainDBContext : DbContext
    {

        public DbSet<User> Users { get; set; }
        public DbSet<Question> Questions { get; set; }
        public DbSet<Answer> Answers { get; set; }
        public DbSet<UserAnswer> UserAnswers { get; set; }
        public DbSet<Statisics> Statisics { get; set; }
        public DbSet<TestResult> TestResults { get; set; }
        public DbSet<RightQuestions> RightQuestions { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            base.OnConfiguring(optionsBuilder);
            optionsBuilder.UseSqlServer(@"Data Source=DESKTOP-SE8E199\SQLEXPRESS;Initial Catalog=ElectroTest;Integrated Security=True");
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            modelBuilder.Entity<Statisics>()
                .HasOne(x => x.User)
                .WithMany(x => x.Statisics)
                .HasForeignKey(x => x.UserID);

            modelBuilder.Entity<Statisics>()
                .HasOne(x => x.TestResult)
                .WithMany(x => x.Statisics)
                .HasForeignKey(x => x.TestResultID);

            modelBuilder.Entity<UserAnswer>()
                .HasOne(x => x.TestResult)
                .WithMany(x => x.UserAnswers)
                .HasForeignKey(x => x.TestResultID);

            modelBuilder.Entity<UserAnswer>()
                .HasOne(x => x.Question)
                .WithMany(x => x.UserAnswers)
                .HasForeignKey(x => x.QuestionID);

            modelBuilder.Entity<UserAnswer>()
                .HasOne(x => x.Answer)
                .WithMany(x => x.UserAnswers)
                .HasForeignKey(x => x.AnswerID);

            modelBuilder.Entity<Answer>()
                .HasOne(x => x.Question)
                .WithMany(x => x.Answers)
                .HasForeignKey(x => x.QuestionID);

            modelBuilder.Entity<RightQuestions>()
                .HasOne(x => x.Answer)
                .WithMany(x => x.RightQuestions)
                .HasForeignKey(x => x.AnswerID);

            modelBuilder.Entity<RightQuestions>()
                .HasOne(x => x.Question)
                .WithMany(x => x.RightQuestions)
                .HasForeignKey(x => x.QuestionID);
        }
    }
}
