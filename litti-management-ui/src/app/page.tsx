"use client"; // This is a client component 
// add bootstrap css 

import 'bootstrap/dist/css/bootstrap.min.css';
import ModelContainerComponent from '@/app/components/models/container/page';
import RootContainerLayout from '@/app/components/container/layout';

export default function Home() {
    return <RootContainerLayout>
        <ModelContainerComponent></ModelContainerComponent>
    </RootContainerLayout>
}