import './App.css'
import CognitoSignUpForm from "./components/CognitoSignUpForm.jsx";
import { BrowserRouter, Routes, Route } from "react-router"
import SignInForm from "./components/SignInForm.jsx";

function App() {

  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<CognitoSignUpForm />} />
          <Route path="login" element={<SignInForm />} />
        </Routes>

      </BrowserRouter>

  )
}

export default App
