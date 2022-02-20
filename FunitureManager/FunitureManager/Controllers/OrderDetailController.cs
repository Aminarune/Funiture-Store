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
    public class OrderDetailController : ApiController
    {
        private FunitureStoreDBContext db = new FunitureStoreDBContext();

        // GET: api/OrderDetail
        public IQueryable<Order_Detail> GetOrder_Detail()
        {
            return db.Order_Detail;
        }

        // GET: api/OrderDetail/5
        [ResponseType(typeof(Order_Detail))]
        public IHttpActionResult GetOrder_Detail(Guid id)
        {
            Order_Detail order_Detail = db.Order_Detail.Find(id);
            if (order_Detail == null)
            {
                return NotFound();
            }

            return Ok(order_Detail);
        }

        // PUT: api/OrderDetail/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutOrder_Detail(Guid id, Order_Detail order_Detail)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != order_Detail.Id_Order)
            {
                return BadRequest();
            }

            db.Entry(order_Detail).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!Order_DetailExists(id))
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

        // POST: api/OrderDetail
        [ResponseType(typeof(Order_Detail))]
        public IHttpActionResult PostOrder_Detail(Order_Detail order_Detail)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Order_Detail.Add(order_Detail);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateException)
            {
                if (Order_DetailExists(order_Detail.Id_Order))
                {
                    return Conflict();
                }
                else
                {
                    throw;
                }
            }

            return CreatedAtRoute("DefaultApi", new { id = order_Detail.Id_Order }, order_Detail);
        }

        // DELETE: api/OrderDetail/5
        [ResponseType(typeof(Order_Detail))]
        public IHttpActionResult DeleteOrder_Detail(Guid id)
        {
            Order_Detail order_Detail = db.Order_Detail.Find(id);
            if (order_Detail == null)
            {
                return NotFound();
            }

            db.Order_Detail.Remove(order_Detail);
            db.SaveChanges();

            return Ok(order_Detail);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool Order_DetailExists(Guid id)
        {
            return db.Order_Detail.Count(e => e.Id_Order == id) > 0;
        }
    }
}