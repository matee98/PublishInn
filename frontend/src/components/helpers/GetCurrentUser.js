import jwtDecode from "jwt-decode";

export const getCurrentUser = () => {
    let user = {
        roles: ["GUEST"]
    };
    let token = localStorage.getItem("token");
    if (token !== undefined && token !== "" && token !== null) {
        user = jwtDecode(token)
    }

    return user;
}