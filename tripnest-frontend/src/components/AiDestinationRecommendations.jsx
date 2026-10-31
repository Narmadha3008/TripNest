import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import API from "../services/api";
import Navbar from "./Navbar";
import Footer from "./Footer";
import "../styles/AiFeatures.css";

const CLIMATE_OPTIONS = [
    { id: "Cool / Mountain", label: "🏔️ Cool Mountain & Hills" },
    { id: "Coastal / Beach", label: "🏖️ Coastal & Tropical Beaches" },
    { id: "Heritage / Moderate", label: "🏰 Royal Heritage & Bazaars" },
    { id: "Tropical", label: "🌴 Tropical Backwaters & Nature" },
];

const INTEREST_OPTIONS = [
    "Nature", "Adventure", "Beaches", "Heritage", "Temples", "Shopping", "Food", "Relaxation"
];

export default function AiDestinationRecommendations() {
    const navigate = useNavigate();

    const [climate, setClimate] = useState("Coastal / Beach");
    const [selectedInterests, setSelectedInterests] = useState(["Beaches", "Food", "Relaxation"]);
    const [budget, setBudget] = useState(30000);
    const [durationDays, setDurationDays] = useState(4);
    const [loading, setLoading] = useState(false);
    const [recommendations, setRecommendations] = useState([]);
    const [favorites, setFavorites] = useState([]);

    useEffect(() => {
        fetchFavorites();
        fetchRecommendations();
    }, []);

    const fetchFavorites = async () => {
        try {
            const res = await API.get("/ai/destinations/favorites");
            if (res.data) setFavorites(res.data);
        } catch (err) {
            console.error("Fetch favorites error:", err);
        }
    };

    const fetchRecommendations = async () => {
        setLoading(true);
        try {
            const payload = {
                climate: climate,
                interests: selectedInterests,
                budget: parseFloat(budget) || 30000,
                durationDays: parseInt(durationDays) || 4,
            };
            const res = await API.post("/ai/destinations/recommend", payload);
            setRecommendations(res.data || []);
        } catch (err) {
            console.error("Fetch recommendations error:", err);
        } finally {
            setLoading(false);
        }
    };

    const toggleInterest = (item) => {
        if (selectedInterests.includes(item)) {
            setSelectedInterests(selectedInterests.filter((i) => i !== item));
        } else {
            setSelectedInterests([...selectedInterests, item]);
        }
    };

    const handleToggleFavorite = async (destName) => {
        try {
            const res = await API.post("/ai/destinations/favorites/toggle", { destinationName: destName });
            if (res.data) {
                if (res.data.isFavorite) {
                    setFavorites([...favorites, destName]);
                } else {
                    setFavorites(favorites.filter((f) => f.toLowerCase() !== destName.toLowerCase()));
                }
            }
        } catch (err) {
            console.error("Toggle favorite error:", err);
        }
    };

    const handleSendToPlanner = (destName) => {
        navigate(`/plan-trip?destination=${encodeURIComponent(destName)}`);
    };
    return (
        <div className="page-wrapper bg-light min-vh-100">
            <Navbar />

            <div className="container py-4">
                <div className="ai-hero-header">
                    <span className="ai-badge mb-2">🧭 Personalized Discovery</span>
                    <h2 className="fw-bold mb-2">AI Destination Recommendations</h2>
                    <p className="lead mb-0 opacity-90 text-light">
                        Discover top-tier destinations matched to your preferred climate, travel interests, and budget.
                    </p>
                </div>

                {/* Filter and Criteria Card */}
                <div className="ai-glass-card p-4 mb-4">
                    <div className="row g-3">
                        <div className="col-md-6">
                            <label className="form-label small fw-bold text-dark">
                                🌤️ Preferred Climate / Scenery:
                            </label>
                            <select
                                className="form-select rounded-3"
                                value={climate}
                                onChange={(e) => setClimate(e.target.value)}
                            >
                                {CLIMATE_OPTIONS.map((c) => (
                                    <option key={c.id} value={c.id}>{c.label}</option>
                                ))}
                            </select>
                        </div>

                        <div className="col-md-3">
                            <label className="form-label small fw-bold text-dark">
                                ⏳ Duration: {durationDays} Days
                            </label>
                            <input
                                type="range"
                                className="form-range"
                                min="2"
                                max="14"
                                value={durationDays}
                                onChange={(e) => setDurationDays(e.target.value)}
                            />
                        </div>

                        <div className="col-md-3">
                            <label className="form-label small fw-bold text-dark">
                                💵 Budget: ₹{Number(budget).toLocaleString()}
                            </label>
                            <input
                                type="range"
                                className="form-range"
                                min="10000"
                                max="100000"
                                step="5000"
                                value={budget}
                                onChange={(e) => setBudget(e.target.value)}
                            />
                        </div>

                        <div className="col-12 mt-2">
                            <label className="form-label small fw-bold text-dark d-block">
                                🎯 Preferred Travel Interests:
                            </label>
                            <div className="d-flex flex-wrap gap-2">
                                {INTEREST_OPTIONS.map((item) => (
                                    <button
                                        key={item}
                                        type="button"
                                        className={`preference-chip ${selectedInterests.includes(item) ? "selected" : ""}`}
                                        onClick={() => toggleInterest(item)}
                                    >
                                        {item}
                                    </button>
                                ))}
                            </div>
                        </div>

                        <div className="col-12 text-end mt-3">
                            <button
                                type="button"
                                className="btn btn-primary rounded-pill px-4 fw-bold"
                                onClick={fetchRecommendations}
                                disabled={loading}
                            >
                                {loading ? "Finding Recommendations..." : "✨ Update Recommendations"}
                            </button>
                        </div>
                    </div>
                </div>

                {/* Recommendations Grid */}
                {loading ? (
                    <div className="text-center py-5">
                        <div className="pulse-spinner text-primary" style={{ width: "3rem", height: "3rem" }}></div>
                        <p className="mt-2 text-muted">Analyzing destinations and calculating match scores...</p>
                    </div>
                ) : (
                    <div className="row g-4 mb-5">
                        {recommendations.map((dest) => {
                            const isFav = favorites.some((f) => f.toLowerCase() === dest.name.toLowerCase());

                            return (
                                <div key={dest.id || dest.name} className="col-md-6 col-lg-4">
                                    <div className="card h-100 border-0 rounded-4 shadow-sm overflow-hidden d-flex flex-column">
                                        <div
                                            className="destination-card-hero"
                                            style={{ backgroundImage: `url(${dest.imageUrl || "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?w=600"})` }}
                                        >
                                            <div className="destination-card-overlay">
                                                <div className="d-flex justify-content-between align-items-center">
                                                    <span className="badge bg-success-subtle text-success border border-success rounded-pill fw-bold px-2 py-1">
                                                        ⭐ {dest.matchScore}% Match
                                                    </span>
                                                    <button
                                                        type="button"
                                                        className={`btn-favorite ${isFav ? "active" : ""}`}
                                                        onClick={() => handleToggleFavorite(dest.name)}
                                                        title={isFav ? "Remove from Favorites" : "Add to Favorites"}
                                                    >
                                                        {isFav ? "❤️" : "🤍"}
                                                    </button>
                                                </div>
                                                <div>
                                                    <h4 className="fw-bold text-white mb-0">{dest.name}</h4>
                                                    <small className="text-light opacity-90">{dest.country} • {dest.category}</small>
                                                </div>
                                            </div>
                                        </div>

                                        <div className="card-body p-3 d-flex flex-column justify-content-between">
                                            <div>
                                                <p className="text-muted small mb-2">{dest.description}</p>

                                                <div className="p-2 bg-light rounded-3 border mb-3">
                                                    <small className="fw-bold text-primary d-block mb-1">💡 Why this matches you:</small>
                                                    <small className="text-secondary">{dest.matchReason}</small>
                                                </div>

                                                <div className="mb-2">
                                                    <small className="text-muted d-block mb-1 fw-bold">Top Attractions:</small>
                                                    <div className="d-flex flex-wrap gap-1">
                                                        {dest.keyAttractions?.slice(0, 3).map((att, i) => (
                                                            <span key={i} className="badge bg-secondary-subtle text-secondary small rounded-pill">
                                                                {att}
                                                            </span>
                                                        ))}
                                                    </div>
                                                </div>

                                                <div className="d-flex justify-content-between align-items-center small text-muted pt-2 border-top">
                                                    <span>🕒 Best: <strong>{dest.bestTimeToVisit}</strong></span>
                                                    <span>💰 {dest.estimatedBudget}</span>
                                                </div>
                                            </div>

                                            <div className="mt-3 pt-2">
                                                <button
                                                    type="button"
                                                    className="btn btn-outline-primary w-100 rounded-pill fw-bold small"
                                                    onClick={() => handleSendToPlanner(dest.name)}
                                                >
                                                    ✨ Plan Expedition to {dest.name}
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                )}
            </div>

            <Footer />
        </div>
    );
}