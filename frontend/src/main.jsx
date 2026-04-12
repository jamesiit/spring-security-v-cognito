import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { Amplify } from "aws-amplify"
import './index.css'
import App from './App.jsx'

//configure on app mount
Amplify.configure({
    Auth: {
        Cognito: {
            userPoolId: import.meta.env.VITE_USER_POOL_ID,
            userPoolClientId: import.meta.env.VITE_APP_CLIENT_ID,
        }
    }
})

createRoot(document.getElementById('root')).render(
    <StrictMode>
    <App />
  </StrictMode>,
)
