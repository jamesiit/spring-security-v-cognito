import {useContext} from "react";
import {UserContext} from "../context/UserProvider.jsx";

export default function TestAuth() {
    const context = useContext(UserContext)
    console.log(context)
}