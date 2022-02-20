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
    public class CategoriesController : Controller
    {
        private FunitureStoreDBContext db = new FunitureStoreDBContext();
        private FunitureStoreDBContext dbs = new FunitureStoreDBContext();

        // GET: Categories
        public ActionResult Index(string sortOrder, string searchString)
        {
            ViewBag.NameSortParm = String.IsNullOrEmpty(sortOrder) ? "name_desc" : "";
            var Categories = from s in db.Categories
                             select s;
            if (!String.IsNullOrEmpty(searchString))
            {
                Categories = Categories.Where(s => s.Category_Name.Contains(searchString));
            }
            switch (sortOrder)
            {
                case "name_desc":
                    Categories = Categories.OrderByDescending(s => s.Category_Name);
                    break;
                default:
                    Categories = Categories.OrderBy(s => s.Category_Name);
                    break;
            }
            Categories = Categories.Where(p => p.Status == true);
            return View(Categories.ToList());
        }

        // GET: Categories/Details/5
        public ActionResult Details(Guid? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Category category = db.Categories.Find(id);
            if (category == null)
            {
                return HttpNotFound();
            }
            return View(category);
        }

        // GET: Categories/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: Categories/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "Id,Category_Name,Status")] Category category, HttpPostedFileBase image1)
        {
            if (ModelState.IsValid)
            {
                category.Picture = new byte[image1.ContentLength];
                image1.InputStream.Read(category.Picture, 0, image1.ContentLength);
                category.Id = Guid.NewGuid();
                db.Categories.Add(category);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(category);
        }

        // GET: Categories/Edit/5
        public ActionResult Edit(Guid? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Category category = db.Categories.Find(id);
            if (category == null)
            {
                return HttpNotFound();
            }
            return View(category);
        }

        // POST: Categories/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "Id,Category_Name,Status")] Category category, HttpPostedFileBase image1)
        {
            if (ModelState.IsValid)
            {
                if (image1 == null)
                {
                    
                    Guid id = category.Id;
                    Category c = dbs.Categories.Find(id);
                    byte[] pic = c.Picture;
                    category.Picture = pic;
                }
                else 
                { 
                    category.Picture = new byte[image1.ContentLength];
                    image1.InputStream.Read(category.Picture, 0, image1.ContentLength);
                }
                db.Entry(category).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(category);
        }

        // GET: Categories/Delete/5
        public ActionResult Delete(Guid? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Category category = db.Categories.Find(id);
            if (category == null)
            {
                return HttpNotFound();
            }
            return View(category);
        }

        // POST: Categories/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(Guid id)
        {
            Category category = db.Categories.Find(id);
            if (category.Status == true) 
            { 
                category.Status = false;
            }
            else
            {
                category.Status = true;
            }
            db.Entry(category).State = EntityState.Modified;
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
        public ActionResult Deleted(string sortOrder, string searchString)
        {
            ViewBag.NameSortParm = String.IsNullOrEmpty(sortOrder) ? "name_desc" : "";
            var Categories = from s in db.Categories
                             select s;
            if (!String.IsNullOrEmpty(searchString))
            {
                Categories = Categories.Where(s => s.Category_Name.Contains(searchString));
            }
            switch (sortOrder)
            {
                case "name_desc":
                    Categories = Categories.OrderByDescending(s => s.Category_Name);
                    break;
                default:
                    Categories = Categories.OrderBy(s => s.Category_Name);
                    break;
            }
            Categories = Categories.Where(p => p.Status == false);
            return View(Categories.ToList());
        }
    }
}
