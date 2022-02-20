using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using FunitureManager.Models;

namespace FunitureManager.Controllers
{
    public class ManagerController : ApiController
    {
        private FunitureStoreDBContext db = new FunitureStoreDBContext();

        // GET: api/Manager
        public IQueryable<Manager> GetManagers()
        {
            return db.Managers;
        }

        // GET: api/Manager/5
        [ResponseType(typeof(Manager))]
        public IHttpActionResult GetManager(Guid id)
        {
            Manager manager = db.Managers.Find(id);
            if (manager == null)
            {
                return NotFound();
            }

            return Ok(manager);
        }

        // PUT: api/Manager/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutManager(Guid id, Manager manager)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != manager.Id)
            {
                return BadRequest();
            }

            db.Entry(manager).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ManagerExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/Manager
        [ResponseType(typeof(Manager))]
        public IHttpActionResult PostManager(Manager manager)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Managers.Add(manager);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateException)
            {
                if (ManagerExists(manager.Id))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = manager.Id }, manager);
        }

        // DELETE: api/Manager/5
        [ResponseType(typeof(Manager))]
        public IHttpActionResult DeleteManager(Guid id)
        {
            Manager manager = db.Managers.Find(id);
            if (manager == null)
            {
                return NotFound();
            }

            db.Managers.Remove(manager);
            db.SaveChanges();

            return Ok(manager);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool ManagerExists(Guid id)
        {
            return db.Managers.Count(e => e.Id == id) > 0;
        }
    }
}