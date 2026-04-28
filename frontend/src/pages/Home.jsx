import { useEffect, useState } from 'react'
import { getAllCars } from '../services/carService'
import '../styles/Home.css'

export default function Home() {
  const [cars, setCars] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const fetchCars = async () => {
      try {
        const data = await getAllCars()
        setCars(data)
      } catch (err) {
        setError('Failed to load cars')
        console.error(err)
      } finally {
        setLoading(false)
      }
    }

    fetchCars()
  }, [])

  if (loading) return <div className="container"><p>Loading cars...</p></div>
  if (error) return <div className="container"><p className="error-message">{error}</p></div>

  return (
    <div className="container">
      <h1>Available Cars</h1>
      {cars.length === 0 ? (
        <p>No cars available at the moment.</p>
      ) : (
        <div className="cars-grid">
          {cars.map(car => (
            <div key={car.id} className="car-card">
              <h3>{car.brand} {car.model}</h3>
              <p>Price: ${car.pricePerDay}/day</p>
              <p>Status: {car.available ? 'Available' : 'Not Available'}</p>
              <button disabled={!car.available}>Rent Now</button>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
