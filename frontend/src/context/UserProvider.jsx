import {createContext, useState} from "react";

export const UserContext = createContext(null);

export function UserProvider (props) {
    const [someValue, setUserValue] = useState();


    function handleUserAuth() {

    }

    return <UserContext value={{someValue, handleUserAuth}}>
        {props.children}
    </UserContext>;
}