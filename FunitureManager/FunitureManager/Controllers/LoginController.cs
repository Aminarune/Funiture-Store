using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using FunitureManager.Models;
using FunitureManager.Controllers;

namespace FunitureManager.Controllers
{
    public class LoginController : Controller
    {
        private FunitureStoreDBContext dBContext = new FunitureStoreDBContext();
        // GET: Login
        public ActionResult Indexlogin()
        {
            return View();
        }

        // Login
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Indexlogin(string username, string password)
        {
            if (ModelState.IsValid)
            {
                var data = dBContext.Managers.Where(s => s.Username.Equals(username) && s.Password.Equals(password)).ToList();
                if (data.Count() > 0)
                {
                    return RedirectToAction(
                        nameof(HomeController.Index),
                        ("Home"));
                }
                else
                {
                    ViewBag.error = "Login failed";
                    return RedirectToAction("IndexLogin");
                }
            }
            return View();
        }

        // Logout
        public ActionResult Logout()
        {
            Session.Clear();//remove session
            return RedirectToAction("Login");
        }
    }
}