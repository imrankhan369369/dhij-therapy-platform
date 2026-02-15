import axios from 'axios';
const api=axios.create({
    baseURL:"http://localhost:369",
})

export default api;