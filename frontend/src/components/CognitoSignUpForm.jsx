import { useState } from "react";
import { useForm } from "react-hook-form";
import {z} from "zod"
import {zodResolver} from "@hookform/resolvers/zod";
import { signUp } from "aws-amplify/auth"
import InputOtpForm from "./InputOtpForm.jsx";

export default function CognitoSignUpForm() {

    const [isOTPPhase, setIsOTPPhase] = useState(false)
    const [unconfirmedEmail, setUnconfirmedEmail] = useState("");

    // define the schema for zod
    const schema = z.object({
        email: z.string().email("Please enter a valid email"),
        password: z.string()
            .min(8, "Password should be at least 8 characters!")
            .regex(/[A-Z]/, "Should contain at least 1 uppercase letter")
            .regex(/[a-z]/, "Should contain at least 1 lowercase letter")
            .regex(/[0-9]/, "Should contains at least 1 number")
            .regex(/[^a-zA-Z0-9]/, "Should contain at least 1 special character!")
    })

    const { register,
        handleSubmit,
        formState: {errors, isSubmitting},
        setError
    } = useForm({
        resolver: zodResolver(schema)
    });

    const onSubmitForm = async (data) => {

        try {
            const {isSignUpComplete, userId, nextStep} = await signUp({
                username: data.email,
                password: data.password
            })

            if (nextStep.signUpStep === "CONFIRM_SIGN_UP" && isSignUpComplete === false) {
                setUnconfirmedEmail(data.email)
                setIsOTPPhase(true)
            }
        } catch (error) {
            setError("root", {
                type:"server",
                message: error.message
            })
        }



    }

    return isOTPPhase ? (
      <InputOtpForm email= {unconfirmedEmail} />  ) : (
        <form
            onSubmit={handleSubmit(onSubmitForm)}
            className="w-full max-w-md flex flex-col gap-6 p-8 bg-black rounded-xl shadow-xl shadow-emerald-500/20 mx-auto"
        >

            {errors.root && (
                <div className="w-full p-3 mb-4 bg-red-500/10 border border-red-500/50 rounded-md text-red-500 text-sm text-center">
                    {errors.root.message}
                </div>
            )}

            <div className="flex flex-col gap-1">
                <input
                    {...register("email")}
                    type="text"
                    placeholder="Email"
                    className="w-full px-4 py-3 bg-neutral-900 text-white rounded-md border border-neutral-800 focus:outline-none focus:ring-2 focus:ring-emerald-500 placeholder:text-neutral-500 transition-all"
                />
                {errors.email && (
                    <div className="text-red-500 text-sm ml-1">{errors.email.message}</div>
                )}
            </div>

            <div className="flex flex-col gap-1">
                <input
                    {...register("password")}
                    type="password"
                    placeholder="Password"
                    className="w-full px-4 py-3 bg-neutral-900 text-white rounded-md border border-neutral-800 focus:outline-none focus:ring-2 focus:ring-emerald-500 placeholder:text-neutral-500 transition-all"
                />
                {errors.password && (
                    <div className="text-red-500 text-sm ml-1">{errors.password.message}</div>
                )}
            </div>

            <button
                type="submit"
                disabled={isSubmitting}
                className="w-full px-4 py-3 mt-2 bg-emerald-600 text-white rounded-md font-semibold hover:bg-emerald-500 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
                {isSubmitting ? "Loading..." : "Submit"}
            </button>
        </form>
    )
}