import { GetServerSideProps } from "next/types";

export async function getServerSideProps() {
    return {props: {base_url: process.env.LIITI_MANAGEMENT_SERVER_URL}}
}
