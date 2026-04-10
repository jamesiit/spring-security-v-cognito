import './App.css'
import CognitoSignUpForm from "./components/CognitoSignUpForm.jsx";
import {BrowserRouter, Routes, Route} from "react-router"
import SignInForm from "./components/SignInForm.jsx";
import TestAuth from "./components/TestAuth.jsx";
import {UserProvider} from "./context/UserProvider.jsx";

function App() {

    return (
        <UserProvider>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<CognitoSignUpForm/>}/>
                    <Route path="/login" element={<SignInForm/>}/>
                    <Route path="/hi" element={<TestAuth />}/>
                    <Route path="*" element={<h1 className="text-white">This page was not found.</h1>}/>
                </Routes>
            </BrowserRouter>
        </UserProvider>
    )
}

export default App
