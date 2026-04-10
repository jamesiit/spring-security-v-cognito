import {createContext, useEffect, useState} from "react";
import {getCurrentUser} from 'aws-amplify/auth';

export const UserContext = createContext(null);

export function UserProvider(props) {
    const [userData, setUserData] = useState(null);
    const [isLoading, setIsLoading] = useState(true)

    const fetchUserSession = async () => {
        setIsLoading(true)
        try {

            const {username, userId, signInDetails} = await getCurrentUser();

            console.log("username", username);
            console.log("user id", userId);
            console.log("sign-in details", signInDetails);

            setUserData(signInDetails.loginId)
            setIsLoading(false)

        } catch (error) {
            console.log("There is no active session!", error)
            setUserData(null)
            setIsLoading(false)
        }

    }

    useEffect(() => {
        fetchUserSession()
    }, [])


    console.log(userData)

    return <UserContext value={{userData, isLoading, fetchUserSession}}>
        {props.children}
    </UserContext>;
}