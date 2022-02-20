using FunitureManager.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Script.Services;
using System.Web.Services;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace FunitureManager.Controllers
{
    public class AnalyticsController : Controller
    {
        private FunitureStoreDBContext db = new FunitureStoreDBContext();
        // GET: Analytics

        public ViewResult Indexanalytics(string year,string year2)
        { 
            //year1
            List<int> ye = new List<int>();
            int year1 = DateTime.Now.Year;
            ViewModel mymodel = new ViewModel();
            if (year == null)
            {
                year = year1.ToString();
            }
            for(int i = 0; i<8; i++)
            {
                ye.Add(year1-i);
            }
            mymodel.Years = ye.ToList();
            int years = Int32.Parse(year);
            //year2
            List<int> ye2 = new List<int>();
            if (year2 == null)
            {
                year2 = year1.ToString();
            }
            for (int i = 0; i < 8; i++)
            {
                ye2.Add(year1 - i);
            }
            mymodel.Years = ye2.ToList();
            int years2 = Int32.Parse(year2);
            ViewBag.YEAR = year;
            ViewBag.YEAR2 = year2;
            //
            List<int> listint = new List<int>();
            List<String> liststring = new List<String>();
            //product sold
            var all = db.Order_Detail.GroupBy(x => x.Id_Product)
                        .Select(x => new
                        {
                            id = x.Key,
                            q = x.Sum(y => y.Quantity)
                        }).OrderByDescending(z => z.q).ToList();
            foreach (var row in all)
            {
                Product p = db.Products.Find(row.id);
                listint.Add(row.q);
                liststring.Add(p.Name);
                //System.Diagnostics.Debug.WriteLine("{0}: {1}", p.Name,row.q);
            }
            ViewBag.INT = listint;
            ViewBag.STRING = liststring;
            //order
            List<int> listint1 = new List<int>();
            for (int i = 0; i < 12; i++)
            {
                var orders = db.Orders.Where(x => x.Date.Year == years).Where(x => x.Date.Month == i+1).Count();
                System.Diagnostics.Debug.WriteLine("{0}: {1}", i+1,orders);
                listint1.Add(orders);
            }
            ViewBag.ORDER = listint1;
            //order2
            List<int> listint12 = new List<int>();
            for (int i = 0; i < 12; i++)
            {
                var orders = db.Orders.Where(x => x.Date.Year == years2).Where(x => x.Date.Month == i + 1).Count();
                System.Diagnostics.Debug.WriteLine("{0}: {1}", i + 1, orders);
                listint12.Add(orders);
            }
            ViewBag.ORDER2 = listint12;
            //profit
            List<decimal?> listdec = new List<decimal?>();
            for (int i = 0; i < 12; i++)
            {
                decimal orders;
                try { 
                    orders = db.Orders.Where(x => x.Date.Year == years).Where(x => x.Date.Month == i + 1).Sum(x=>x.Price);
                    listdec.Add(orders);
                }
                catch (InvalidOperationException e)
                {
                    orders = 0;
                    listdec.Add(orders);
                }
            }
            ViewBag.PROFIT = listdec;
            //profit2
            List<decimal?> listdec2 = new List<decimal?>();
            for (int i = 0; i < 12; i++)
            {
                decimal orders;
                try
                {
                    orders = db.Orders.Where(x => x.Date.Year == years2).Where(x => x.Date.Month == i + 1).Sum(x => x.Price);
                    listdec2.Add(orders);
                }
                catch (InvalidOperationException e)
                {
                    orders = 0;
                    listdec2.Add(orders);
                }
            }
            ViewBag.PROFIT2 = listdec2;
            //favorite
            List<int> listint2 = new List<int>();
            List<String> liststring2 = new List<String>();
            var fav = db.Favorites.GroupBy(x => x.Id_Product)
                        .Select(x => new
                        {
                            id = x.Key,
                            q = x.Count()
                        }).OrderByDescending(z => z.q).ToList();
            foreach (var row in fav)
            {
                Product p = db.Products.Find(row.id);
                listint2.Add(row.q);
                liststring2.Add(p.Name);
                System.Diagnostics.Debug.WriteLine("{0}: {1}", p.Name,row.q);
            }
            ViewBag.INT2 = listint2;
            ViewBag.STRING2 = liststring2;
            //compare

            return View(mymodel);
        }
    }
}