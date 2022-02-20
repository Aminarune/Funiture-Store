using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using FunitureManager.Models;

namespace FunitureManager.Controllers
{
    public class ProductsController : Controller
    {
        private FunitureStoreDBContext db = new FunitureStoreDBContext();
        private FunitureStoreDBContext dbs = new FunitureStoreDBContext();

        // GET: Products
        public ViewResult Index(string sortOrder, string searchString)
        {
            ViewBag.NameSortParm = String.IsNullOrEmpty(sortOrder) ? "name_desc" : "";
            ViewBag.CateSortParm = sortOrder == "Cate" ? "Cate_desc" : "Cate";
            ViewBag.PriceSortParm = sortOrder == "Price" ? "Price_desc" : "Price";
            var products = from s in db.Products
                           select s;
            if (!String.IsNullOrEmpty(searchString))
            {
                products = products.Where(s => s.Name.Contains(searchString)
                    ||s.Category.Category_Name.Contains(searchString));
            }
            switch (sortOrder)
            {
                case "name_desc":
                    products = products.OrderByDescending(s => s.Name);
                    break;
                case "Cate":
                    products = products.OrderBy(s => s.Category.Category_Name);
                    break;
                case "Cate_desc":
                    products = products.OrderByDescending(s => s.Category.Category_Name);
                    break;
                case "Price":
                    products = products.OrderBy(s => s.Price);
                    break;
                case "Price_desc":
                    products = products.OrderByDescending(s => s.Price);
                    break;
                default:
                    products = products.OrderBy(s => s.Name);
                    break;
            }
                products = products.Where(p => p.Status == true);
            return View(products.ToList());
        }

        // GET: Products/Details/5
        public ActionResult Details(Guid? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Product product = db.Products.Find(id);
            if (product == null)
            {
                return HttpNotFound();
            }
            return View(product);
        }

        // GET: Products/Create
        public ActionResult Create()
        {
            ViewBag.Id_Category = new SelectList(db.Categories, "Id", "Category_Name");
            return View();
        }

        // POST: Products/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "Id,Name,Id_Category,Price,Description,Status")] Product product,HttpPostedFileBase image1)
        {
            if (ModelState.IsValid)
            {
                product.Picture = new byte[image1.ContentLength];
                image1.InputStream.Read(product.Picture, 0, image1.ContentLength);
                product.Id = Guid.NewGuid();
                db.Products.Add(product);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.Id_Category = new SelectList(db.Categories, "Id", "Category_Name", product.Id_Category);
            return View(product);
        }

        // GET: Products/Edit/5
        public ActionResult Edit(Guid? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Product product = db.Products.Find(id);
            if (product == null)
            {
                return HttpNotFound();
            }
            ViewBag.Id_Category = new SelectList(db.Categories, "Id", "Category_Name", product.Id_Category);
            return View(product);
        }

        // POST: Products/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "Id,Name,Id_Category,Price,Description,Status")] Product product, HttpPostedFileBase image1)
        {
            if (ModelState.IsValid)
            {
                if (image1 == null)
                {

                    Guid id = product.Id;
                    Product c = dbs.Products.Find(id);
                    byte[] pic = c.Picture;
                    product.Picture = pic;
                }
                else
                {
                    product.Picture = new byte[image1.ContentLength];
                    image1.InputStream.Read(product.Picture, 0, image1.ContentLength);
                }
                db.Entry(product).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            ViewBag.Id_Category = new SelectList(db.Categories, "Id", "Category_Name", product.Id_Category);
            return View(product);
        }

        // GET: Products/Delete/5
        public ActionResult Delete(Guid? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Product product = db.Products.Find(id);
            if (product == null)
            {
                return HttpNotFound();
            }
            return View(product);
        }

        // POST: Products/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(Guid id)
        {
            Product product = db.Products.Find(id);
            if (product.Status == true)
            {
                product.Status = false;
            }
            else
            {
                product.Status = true;
            }
            db.Entry(product).State = EntityState.Modified;
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
        public ViewResult Deleted(string sortOrder, string searchString)
        {
            ViewBag.NameSortParm = String.IsNullOrEmpty(sortOrder) ? "name_desc" : "";
            ViewBag.CateSortParm = sortOrder == "Cate" ? "Cate_desc" : "Cate";
            ViewBag.PriceSortParm = sortOrder == "Price" ? "Price_desc" : "Price";
            var products = from s in db.Products
                           select s;
            if (!String.IsNullOrEmpty(searchString))
            {
                products = products.Where(s => s.Name.Contains(searchString)
                    || s.Category.Category_Name.Contains(searchString));
            }
            switch (sortOrder)
            {
                case "name_desc":
                    products = products.OrderByDescending(s => s.Name);
                    break;
                case "Cate":
                    products = products.OrderBy(s => s.Category.Category_Name);
                    break;
                case "Cate_desc":
                    products = products.OrderByDescending(s => s.Category.Category_Name);
                    break;
                case "Price":
                    products = products.OrderBy(s => s.Price);
                    break;
                case "Price_desc":
                    products = products.OrderByDescending(s => s.Price);
                    break;
                default:
                    products = products.OrderBy(s => s.Name);
                    break;
            }
            products = products.Where(p => p.Status == false);
            return View(products.ToList());
        }
    }
}
