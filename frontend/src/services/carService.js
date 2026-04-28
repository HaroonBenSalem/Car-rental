import apiClient from './authService'

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

export const getAllCars = async () => {
  const response = await apiClient.get('/cars')
  return response.data
}

export const getCarById = async (id) => {
  const response = await apiClient.get(`/cars/${id}`)
  return response.data
}

export const createCar = async (carData) => {
  const response = await apiClient.post('/cars', carData)
  return response.data
}

export const updateCar = async (id, carData) => {
  const response = await apiClient.put(`/cars/${id}`, carData)
  return response.data
}

export const deleteCar = async (id) => {
  const response = await apiClient.delete(`/cars/${id}`)
  return response.data
}
