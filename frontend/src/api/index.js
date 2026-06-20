import axios from 'axios'

var api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  withCredentials: true
})

// Helper: unwrap axios response .data
function d(fn) {
  return async (...args) => (await fn(...args)).data
}

// Data APIs
export var getPosts = d(() => api.get('/blog'))
export var getPost = (id) => d(() => api.get('/blog/' + id))()
export var getProjects = d(() => api.get('/projects'))
export var getSkills = d(() => api.get('/skills'))

// Contact
export var submitContact = (data) => api.post('/contact', data)

// Comments
export var submitComment = (data) => api.post('/comments', data)
export var getComments = (postId) => d(() => api.get('/comments/list/' + postId))()

// Search
export var search = (q) => d(() => api.get('/search', { params: { q } }))()

// Auth
export var login = (data) => api.post('/auth/login', data)
export var register = (data) => api.post('/auth/register', data)
export var getProfile = d(() => api.get('/auth/profile'))
export var updateProfile = (data) => api.post('/auth/update-profile', data)
export var uploadAvatar = (formData) => api.post('/auth/upload-avatar', formData)
