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
    public class ShippingAddressController : ApiController
    {
        private FunitureStoreDBContext db = new FunitureStoreDBContext();

        // GET: api/ShippingAddress
        public IQueryable<Shipping_Address> GetShipping_Address()
        {
            return db.Shipping_Address;
        }

        // GET: api/ShippingAddress/5
        [ResponseType(typeof(Shipping_Address))]
        public IHttpActionResult GetShipping_Address(Guid id)
        {
            Shipping_Address shipping_Address = db.Shipping_Address.Find(id);
            if (shipping_Address == null)
            {
                return NotFound();
            }

            return Ok(shipping_Address);
        }

        // PUT: api/ShippingAddress/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutShipping_Address(Guid id, Shipping_Address shipping_Address)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != shipping_Address.Id)
            {
                return BadRequest();
            }

            db.Entry(shipping_Address).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!Shipping_AddressExists(id))
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

        // POST: api/ShippingAddress
        [ResponseType(typeof(Shipping_Address))]
        public IHttpActionResult PostShipping_Address(Shipping_Address shipping_Address)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Shipping_Address.Add(shipping_Address);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateException)
            {
                if (Shipping_AddressExists(shipping_Address.Id))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = shipping_Address.Id }, shipping_Address);
        }

        // DELETE: api/ShippingAddress/5
        [ResponseType(typeof(Shipping_Address))]
        public IHttpActionResult DeleteShipping_Address(Guid id)
        {
            Shipping_Address shipping_Address = db.Shipping_Address.Find(id);
            if (shipping_Address == null)
            {
                return NotFound();
            }

            db.Shipping_Address.Remove(shipping_Address);
            db.SaveChanges();

            return Ok(shipping_Address);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool Shipping_AddressExists(Guid id)
        {
            return db.Shipping_Address.Count(e => e.Id == id) > 0;
        }
    }
}