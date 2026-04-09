import './App.css'
import CognitoSignUpForm from "./components/CognitoSignUpForm.jsx";
import {BrowserRouter, Routes, Route} from "react-router"
import SignInForm from "./components/SignInForm.jsx";

function App() {

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<CognitoSignUpForm/>}/>
                <Route path="/login" element={<SignInForm/>}/>
                <Route path="*" element={<h1 className="text-white">This page was not found.</h1>}/>
            </Routes>
        </BrowserRouter>
    )
}

export default App
