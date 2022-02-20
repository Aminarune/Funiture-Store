using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using FunitureManager.Models;
using FunitureManager.Services;

namespace FunitureManager.Controllers
{
    public class OrdersController : Controller
    {
        private FunitureStoreDBContext db = new FunitureStoreDBContext();
        FireBasePush push = new FireBasePush("AAAA7EDld9M:APA91bFG_u9yAD0nga0khbfVrzgWHl3NHBcU5Yheuxlgqu3gv7XTFROi0V9U-lhyaBkrF0WM2PkYc1uo6mTDK5WZZQjnUmNo4ySFA0mIqKHvhxU0m2SDyD5aIYdlioYukGoMvB3WdwJ4");
        // GET: Orders
        public ViewResult Index(string sortOrder, string searchString)
        {
            ViewBag.NameSortParm = String.IsNullOrEmpty(sortOrder) ? "Date" : "";
            ViewBag.StatSortParm = sortOrder == "Stat" ? "Stat_desc" : "Stat";
            ViewBag.DateSortParm = sortOrder == "Name" ? "Name_desc" : "Name";
            ViewBag.PriceSortParm = sortOrder == "Price" ? "Price_desc" : "Price";
            var products = from s in db.Orders
                           select s;
            if (!String.IsNullOrEmpty(searchString))
            {
                products = products.Where(s => s.User.User_Name.Contains(searchString)
                    ||s.State.Contains(searchString));
            }
            switch (sortOrder)
            {
                case "Name_desc":
                    products = products.OrderByDescending(s => s.User.User_Name);
                    break;
                case "Stat":
                    products = products.OrderBy(s => s.Status);
                    break;
                case "Stat_desc":
                    products = products.OrderByDescending(s => s.Status);
                    break;
                case "Date":
                    products = products.OrderBy(s => s.Date);
                    break;
                case "Name":
                    products = products.OrderBy(s => s.User.User_Name);
                    break;
                case "Price":
                    products = products.OrderBy(s => s.Price);
                    break;
                case "Price_desc":
                    products = products.OrderByDescending(s => s.Price);
                    break;
                default:
                    products = products.OrderByDescending(s => s.Date);
                    break;
            }

            return View(products.ToList());
        }
            // GET: Orders/Details/5
            public ActionResult Details(Guid? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Order order = db.Orders.Find(id);
            
            if (order == null)
            {
                return HttpNotFound();
            }
            else
            {
                var order_Detail = db.Order_Detail.Include(o => o.Order).Include(o => o.Product);
                var list = order_Detail.Where(o => o.Id_Order == id);
                return View(list.ToList());
                
            }
        }

        // GET: Orders/Create
        public ActionResult Create()
        {
            ViewBag.Id_Manager = new SelectList(db.Managers, "Id", "Username");
            ViewBag.Id_User = new SelectList(db.Users, "Id", "User_Name");
            return View();
        }

        // POST: Orders/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "Id,Date,Price,Id_User,Id_Manager,Status")] Order order)
        {
            if (ModelState.IsValid)
            {
                order.Id = Guid.NewGuid();
                db.Orders.Add(order);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.Id_Manager = new SelectList(db.Managers, "Id", "Username", order.Id_Manager);
            ViewBag.Id_User = new SelectList(db.Users, "Id", "User_Name", order.Id_User);
            return View(order);
        }

        // GET: Orders/Edit/5
        public ActionResult Edit(Guid? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Order order = db.Orders.Find(id);
            if (order == null)
            {
                return HttpNotFound();
            }
            ViewBag.Id_Manager = new SelectList(db.Managers, "Id", "Username", order.Id_Manager);
            ViewBag.Id_User = new SelectList(db.Users, "Id", "User_Name", order.Id_User);
            return View(order);
        }

        // POST: Orders/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "Id,Date,Price,Id_User,Id_Manager,Status")] Order order)
        {
            if (ModelState.IsValid)
            {
                db.Entry(order).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            ViewBag.Id_Manager = new SelectList(db.Managers, "Id", "Username", order.Id_Manager);
            ViewBag.Id_User = new SelectList(db.Users, "Id", "User_Name", order.Id_User);
            return View(order);
        }

        // GET: Orders/Delete/5
        public ActionResult Delete(Guid id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Order order = db.Orders.Find(id);
            if (order == null)
            {
                return HttpNotFound();
            }
            return View(order);
        }

        // POST: Orders/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(Guid id)
        {
            Order order = db.Orders.Find(id);
            db.Orders.Remove(order);
            db.SaveChanges();
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
        [HttpPost, ActionName("Index")]
        //[ValidateAntiForgeryToken]
        public ActionResult Index(Guid id,String status)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest); 
            }

            Order order = db.Orders.Find(id);
            if (order == null)
            {
                return HttpNotFound();
            }
            if (status == null) 
            { 
                status = "Pending"; 
            }
            order.State = order.State.Replace(order.State, status);
            db.Entry(order).State = EntityState.Modified;
            db.SaveChanges();
            //
            if (order.User.Token != null) 
            { 
                push.SendPush(new PushMessage()
                {
                    to = order.User.Token, //for a topic to": "/topics/foo-bar"

                    notification = new PushMessageData
                    {
                        title = "Your Order is "+status,
                        text = "Your status order now is "+status,
                        click_action =  "click_action" 
                    },
                    data = new
                    {
                        example = "empty"
                    }
                });
            }
            //
            return RedirectToAction("Index");
        }

    }
}
