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
    public class FavoriteController : ApiController
    {
        private FunitureStoreDBContext db = new FunitureStoreDBContext();

        // GET: api/Favorite
        public IQueryable<Favorite> GetFavorites()
        {
            return db.Favorites;
        }

        // GET: api/Favorite/5
        [ResponseType(typeof(Favorite))]
        public IHttpActionResult GetFavorite(Guid id)
        {
            Favorite favorite = db.Favorites.Find(id);
            if (favorite == null)
            {
                return NotFound();
            }

            return Ok(favorite);
        }

        // PUT: api/Favorite/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutFavorite(Guid id, Favorite favorite)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != favorite.Id)
            {
                return BadRequest();
            }

            db.Entry(favorite).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!FavoriteExists(id))
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

        // POST: api/Favorite
        [ResponseType(typeof(Favorite))]
        public IHttpActionResult PostFavorite(Favorite favorite)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Favorites.Add(favorite);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateException)
            {
                if (FavoriteExists(favorite.Id))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = favorite.Id }, favorite);
        }

        // DELETE: api/Favorite/5
        [ResponseType(typeof(Favorite))]
        public IHttpActionResult DeleteFavorite(Guid id)
        {
            Favorite favorite = db.Favorites.Find(id);
            if (favorite == null)
            {
                return NotFound();
            }

            db.Favorites.Remove(favorite);
            db.SaveChanges();

            return Ok(favorite);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool FavoriteExists(Guid id)
        {
            return db.Favorites.Count(e => e.Id == id) > 0;
        }
    }
}