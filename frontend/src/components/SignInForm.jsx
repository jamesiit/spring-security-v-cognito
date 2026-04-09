import { useState } from "react";
import { useForm } from "react-hook-form";
import {z} from "zod"
import {zodResolver} from "@hookform/resolvers/zod";
import { signIn } from "aws-amplify/auth"

export default function SignInForm() {

    const [showPassword, setShowPassword] = useState(false)

    // define the schema for zod
    const schema = z.object({
        email: z.string().email("Please enter a valid email"),
        password: z.string().min(1, "Password is required")
    })

    const { register,
        handleSubmit,
        formState: {errors, isSubmitting},
        setError
    } = useForm({
        resolver: zodResolver(schema)
    });

    function handleShow(event) {
        event.preventDefault()
        setShowPassword(!showPassword)
    }

    const onSubmitForm = async (data) => {

        try {
            const {isSignUpComplete, nextStep} = await signIn ({
                username: data.email,
                password: data.password
            })

            console.log(nextStep.signInStep)

        } catch (error) {
            setError("root", {
                type:"server",
                message: error.message
            })
        }

    }

    return (
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

            <div className="flex flex-col gap-1 relative">
                <input
                    {...register("password")}
                    type={ showPassword ? "text" : "password" }
                    placeholder="Password"
                    className="w-full px-4 py-3 bg-neutral-900 text-white rounded-md border border-neutral-800 focus:outline-none focus:ring-2 focus:ring-emerald-500 placeholder:text-neutral-500 transition-all"
                />
                <button onClick={handleShow} className="absolute right-0 top-1/2 -translate-y-1/2 content-center text-sm font-medium pr-20 text-neutral-400 hover:text-emerald-400 px-6 py-3"> { showPassword ? "Hide" : "Show" } </button>
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