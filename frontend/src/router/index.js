import { createRouter, createWebHistory } from 'vue-router'

const Home = () => import('../views/Home.vue')
const About = () => import('../views/About.vue')
const Blog = () => import('../views/Blog.vue')
const BlogDetail = () => import('../views/BlogDetail.vue')
const Projects = () => import('../views/Projects.vue')
const Contact = () => import('../views/Contact.vue')
const Login = () => import('../views/Login.vue')
const Register = () => import('../views/Register.vue')
const Profile = () => import('../views/Profile.vue')
const Guestbook = () => import('../views/Guestbook.vue')
const Timeline = () => import('../views/Timeline.vue')
const Resume = () => import('../views/Resume.vue')
const Friends = () => import('../views/Friends.vue')
const NotFound = () => import('../views/NotFound.vue')

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/about', name: 'About', component: About },
  { path: '/blog', name: 'Blog', component: Blog },
  { path: '/blog/:id', name: 'BlogDetail', component: BlogDetail },
  { path: '/projects', name: 'Projects', component: Projects },
  { path: '/contact', name: 'Contact', component: Contact },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/profile', name: 'Profile', component: Profile },
  { path: '/guestbook', name: 'Guestbook', component: Guestbook },
  { path: '/timeline', name: 'Timeline', component: Timeline },
  { path: '/resume', name: 'Resume', component: Resume },
  { path: '/friends', name: 'Friends', component: Friends },
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFound }
]

export default createRouter({ history: createWebHistory(), routes })
