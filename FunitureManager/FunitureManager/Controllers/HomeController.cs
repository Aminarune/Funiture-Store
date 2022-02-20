using FunitureManager.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace FunitureManager.Controllers
{
    public class HomeController : Controller
    {
        private FunitureStoreDBContext db = new FunitureStoreDBContext();
        private FunitureStoreDBContext dbs = new FunitureStoreDBContext();
        // GET: Home
        public ViewResult Index()
        {
            /*var products = from s in db.Orders
                           select s;
            products = products.OrderByDescending(s => s.Date);
            return View(products.ToList());*/  
            ViewModel mymodel = new ViewModel();

            //Order
                var order = from s in db.Orders
                            select s;
                order = order.OrderByDescending(s => s.Date);
                 

                mymodel.Orders = order.ToList();

            //Order_detail
            var orders = from s in db.Order_Detail select s;
            mymodel.Order_Details = orders.ToList();

            //test2
            var test = db.Order_Detail.GroupBy(x => x.Id_Product)
                        .Select(x => new
                        {
                            id = x.Key,
                            q = x.Sum(y => y.Quantity)
                        }).OrderByDescending(z => z.q).ToList();
            List<Test2> aa = new List<Test2>();
                foreach (var row in test)
                {
                    System.Diagnostics.Debug.WriteLine("{0}: {1}", row.id, row.q);
                    Product p = db.Products.Find(row.id);
                    var bb = new Test2(p.Name, p.Picture, p.Price);
                    aa.Add(bb);
                }
            mymodel.Tests2 = aa;

            //test1
            var test1 = db.Orders.Where(x => x.Date.Day == DateTime.Today.Day).Count();
            var test2 = from c in db.Orders.Where(so => so.Date.Day == DateTime.Today.Day) 
                        select new
                        {
                            w = c.Order_Detail.Sum(x=>x.Quantity),
                            e = c.Order_Detail.Sum(x => x.Total_Price)
                        };
            List<Test1> a = new List<Test1>();
                foreach (var row in test2)
                {
                    var b = new Test1(test1, test2.Sum(x => x.w), test2.Sum(x => x.e), 0);
                    System.Diagnostics.Debug.WriteLine("{0}: {1}", row.w, row.e);
                    a.Add(b);
                break;
                }
            mymodel.Tests1 = a;
            return View(mymodel);

        }

        // GET: Home/Details/5
        public ActionResult Details(int id)
        {
            return View();
        }

        // GET: Home/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: Home/Create
        [HttpPost]
        public ActionResult Create(FormCollection collection)
        {
            try
            {
                // TODO: Add insert logic here

                return RedirectToAction("Index");
            }
            catch
            {
                return View();
            }
        }

        // GET: Home/Edit/5
        public ActionResult Edit(int id)
        {
            return View();
        }

        // POST: Home/Edit/5
        [HttpPost]
        public ActionResult Edit(int id, FormCollection collection)
        {
            try
            {
                // TODO: Add update logic here

                return RedirectToAction("Index");
            }
            catch
            {
                return View();
            }
        }

        // GET: Home/Delete/5
        public ActionResult Delete(int id)
        {
            return View();
        }

        // POST: Home/Delete/5
        [HttpPost]
        public ActionResult Delete(int id, FormCollection collection)
        {
            try
            {
                // TODO: Add delete logic here

                return RedirectToAction("Index");
            }
            catch
            {
                return View();
            }
        }
        /*
        public ViewResult Tests()
        {
            ViewModel mymodel = new ViewModel();
            var test1 = (from c in db.Order_Detail
                         select new { Product = c.Id_Product, Quantity = c.Quantity }).Sum(x => x.Quantity);
            mymodel.Test = test1.ToList();
            return View(test1.ToList());
        }
        */
    }

}
