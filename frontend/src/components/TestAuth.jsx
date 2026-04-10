import {useContext} from "react";
import {UserContext} from "../context/UserProvider.jsx";
import {Navigate} from "react-router";

export default function TestAuth() {

    const context = useContext(UserContext)

    if (context.isLoading) {
        return (
            <h1> Loading... </h1>
        )
    }

    if (context.userData === null) {
        return <Navigate to="/login" replace/>
    }

    return (
    <>
        <h1 className="text-white"> Spring Security vs Cognito? I thought Iran vs US these days! </h1>
        <p className="text-white"> You're authenticated as: </p> <p className="text-emerald-400"> {context.userData} </p>
    </>
    )
}