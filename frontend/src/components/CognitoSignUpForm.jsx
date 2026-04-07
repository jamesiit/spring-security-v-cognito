import { useForm } from "react-hook-form";
import {z} from "zod"
import {zodResolver} from "@hookform/resolvers/zod";

export default function CognitoSignUpForm() {

    // define the schema for zod
    const schema = z.object({
        email: z.string().email("Please enter a valid email"),
        password: z.string().min(8, "Password should be at least 8 characters!")
    })

    const { register,
        handleSubmit,
        formState: {errors, isSubmitting}
    } = useForm({
        resolver: zodResolver(schema)
    });

    const onSubmitForm = async (data) => {
        await new Promise((resolve) => setTimeout(resolve, 2000));
        console.log(data)
    }

    return (
        <form
            onSubmit={handleSubmit(onSubmitForm)}
            className="w-full max-w-md flex flex-col gap-6 p-8 bg-black rounded-xl shadow-xl shadow-emerald-500/20 mx-auto">
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
    );
}