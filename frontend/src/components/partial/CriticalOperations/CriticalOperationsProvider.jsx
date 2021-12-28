import React, {createContext, useContext, useReducer} from "react";
import DialogWindow from "./DialogWindow";
import {v4} from "uuid";

const CriticalOperation = createContext();

const CriticalOperationProvider = (props) => {
    const [state, dispatch] = useReducer((state, action) => {
        switch (action.type) {
            case "ADD_DIALOG":
                return [...state, {...action.payload}];
            case "REMOVE_DIALOG":
                return state.filter(e => e.id !== action.id);
            default:
                return state;
        }
    }, []);

    return (
        <CriticalOperation.Provider value={dispatch}>
            {state.map((note) => {
                return <DialogWindow dispatch={dispatch} key={note.id} {...note} />
            })}
            {props.children}
        </CriticalOperation.Provider>
    )
};

export const useDialog = () => {
    const dispatch = useContext(CriticalOperation);

    return ({title, message, callbackOnSave = (() => {}), callbackOnCancel = (() => {}), ...props}) => {
        dispatch({
            type: "ADD_DIALOG",
            payload: {
                id: v4(),
                title: title,
                message: message,
                callbackOnSave: callbackOnSave,
                callbackOnCancel: callbackOnCancel,
                ...props
            }
        });
    }
};

export const useDialogPermanentChange = () => {
    const dispatch = useContext(CriticalOperation);

    return ({callbackOnSave = (() => {}), callbackOnCancel = (() => {}), ...props}) => {
        dispatch({
            type: "ADD_DIALOG",
            payload: {
                id: v4(),
                title: "Nieodwracalna zmiana",
                message: "Wprowadzone zmiany nie będą mogły zostać cofnięte",
                callbackOnSave: callbackOnSave,
                callbackOnCancel: callbackOnCancel,
                ...props
            }
        });
    }
};


export default CriticalOperationProvider;