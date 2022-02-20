using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace FunitureManager.Controllers
{
    public class ForgotpasswordController : Controller
    {
        // GET: Forgotpassword
        public ActionResult IndexResetPW()
        {
            return View();
        }
    }
}