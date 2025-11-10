// /src/pages/HomePage.jsx
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

const isUserLoggedIn = false;

function HomePage() {
  const navigate = useNavigate();
  const [eventType, setEventType] = useState('');
  const [location, setLocation] = useState('');
  const [date, setDate] = useState('');

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    // Điều hướng sang trang Browse kèm query, ví dụ: /browse-chefs?event=...&loc=...&date=...
    const params = new URLSearchParams();
    if (eventType) params.set('event', eventType);
    if (location) params.set('loc', location);
    if (date) params.set('date', date);
    navigate(`/browse-chefs?${params.toString()}`);
  };

  return (
    <div className="homepage-wrapper">
      {/* HEADER */}
      <header className="navbar navbar-expand-lg navbar-dark sticky-top navbar-luxury">
        <div className="container">
          <Link to="/" className="navbar-brand nav-logo">
            <img src="/logo.png" alt="ChefBooking Logo" />
          </Link>

          <ul className="navbar-nav mx-auto">
            <li className="nav-item">
              <Link to="/browse-chefs" className="nav-link">Browse Chefs</Link>
            </li>
            <li className="nav-item">
              <Link to="/reviews" className="nav-link">Reviews</Link>
            </li>
          </ul>

          <div className="d-flex align-items-center">
            <Link
              to={isUserLoggedIn ? "/profile" : "/login"}
              className="user-icon-button me-3"
              title={isUserLoggedIn ? "My Account" : "Log In"}
            >
              <span>👤</span>
            </Link>
            <Link to="/apply-as-chef" className="nav-button nav-button-outline me-2">
              Become a chef
            </Link>
            <Link to="/browse-chefs" className="nav-button nav-button-solid d-none d-lg-block">
              Find local chef
            </Link>
          </div>
        </div>
      </header>

      {/* MAIN */}
      <main className="main-content">
        <div className="container">
          {/* HERO + SEARCH */}
          <section className="hero-section text-center">
            <h1 className="hero-title">Find & Book a Private Chef for Your Event</h1>
            <p className="hero-subtitle">Get quotes from top-rated chefs today.</p>

            <form className="search-form mt-4" onSubmit={handleSearchSubmit}>
              <input
                type="text"
                placeholder="Event Type (e.g., Birthday)"
                value={eventType}
                onChange={(e) => setEventType(e.target.value)}
              />
              <input
                type="text"
                placeholder="Location"
                value={location}
                onChange={(e) => setLocation(e.target.value)}
              />
              <input
                type="date"
                value={date}
                onChange={(e) => setDate(e.target.value)}
              />
              <button type="submit">Search Chefs</button>
            </form>
          </section>

          {/* HOW IT WORKS */}
          <section className="how-it-works-section text-center">
            <h2 className="section-title">How it Works</h2>
            <div className="row mt-5">
              <div className="col-md-4">
                <div className="step-icon">1</div>
                <h3>1. Search</h3>
                <p>Find chefs or services that match your needs.</p>
              </div>
              <div className="col-md-4">
                <div className="step-icon">2</div>
                <h3>2. Get Quotes</h3>
                <p>Receive custom menus and quotes.</p>
              </div>
              <div className="col-md-4">
                <div className="step-icon">3</div>
                <h3>3. Book</h3>
                <p>Confirm your event and enjoy the experience.</p>
              </div>
            </div>
          </section>

          {/* FEATURED CHEFS */}
          <section className="featured-chefs-section">
            <h2 className="section-title text-center">Meet Our Top Chefs</h2>
            <div className="placeholder-content">
              (This block will contain featured chef profiles using Bootstrap Cards)
            </div>
          </section>

          {/* SERVICES */}
          <section className="featured-services-section">
            <h2 className="section-title text-center">Explore Our Services</h2>
            <div className="placeholder-content">
              (This block will contain service cards: "BBQ Party", "Fine Dining", etc.)
            </div>
          </section>
        </div>
      </main>

      {/* FOOTER */}
      <footer className="footer-luxury text-center">
        <p>&copy; 2025 ChefBooking. All rights reserved.</p>
      </footer>
    </div>
  );
}

export default HomePage;
