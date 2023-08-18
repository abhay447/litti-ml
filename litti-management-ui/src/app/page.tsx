"use client"; // This is a client component 
// add bootstrap css 

import 'bootstrap/dist/css/bootstrap.min.css';
import ModelContainerComponent from '@/app/components/models/container/page';
import RootContainerLayout from '@/app/components/container/layout';
import { useState } from 'react';
import { NAV_OPTION_FEATURES, NAV_OPTION_MODELS } from '@/app/components/sidebar/products/page';
import FeatureContainerComponent from '@/app/components/features/container/page';

export default function Home() {
    let [navOption, setNavOption] = useState(NAV_OPTION_MODELS);

    function renderCurrentComponent() {
        switch(navOption) {
            case NAV_OPTION_FEATURES:
                return <FeatureContainerComponent/>
            default:
                return <ModelContainerComponent/>
        }
    }
    return <RootContainerLayout navOption={navOption} setNavOption={setNavOption}>
        {renderCurrentComponent()}
    </RootContainerLayout>
}