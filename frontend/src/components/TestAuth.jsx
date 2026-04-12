import {useContext} from "react";
import {UserContext} from "../context/UserProvider.jsx";
import {Navigate} from "react-router";
import {useQuery} from "@tanstack/react-query";
import { fetchAuthSession } from 'aws-amplify/auth';

export default function TestAuth() {

    const context = useContext(UserContext)
    const { isPending, error, data } = useQuery({
        queryKey: ['hi-endpoint'],
        queryFn: async () => {
            // grab the session
            const session = await fetchAuthSession()
            const token = session.tokens?.accessToken?.toString()

            // attach as a header
            const headers = {
                "Content-Type": "application/json"
            }

            if (token) {
                headers["Authorization"] = `Bearer ${token}`
            }

            // fire it
            const response = await fetch("http://localhost:8080/hi", {
                method: "GET",
                headers: headers
            })

            if (!response.ok) {
                throw new Error(`Spring Boot rejected the request: ${response.status}`)
            }

            const extractedText = await response.text()
            console.log("Response from Spring Boot: ", extractedText)

            return extractedText


        }
    })

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
        <br />
        <p className="text-white"> You're authenticated as: </p> <p className="text-emerald-400"> {context.userData} </p>
        <p className="text-white"> And your AWS UUID is: </p> <p className="text-emerald-400"> {data} </p>
    </>
    )
}