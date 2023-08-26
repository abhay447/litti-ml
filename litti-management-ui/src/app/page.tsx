"use client"; // This is a client component 
// add bootstrap css 

import 'bootstrap/dist/css/bootstrap.min.css';
import ModelContainerComponent from '@/app/components/models/container/page';
import RootContainerLayout from '@/app/components/container/layout';
import { useState } from 'react';
import FeatureContainerComponent from '@/app/components/features/container/page';
import FeatureGroupContainerComponent from '@/app/components/feature-groups/container/page';
import { NAV_OPTION_MODELS, NAV_OPTION_FEATURES, NAV_OPTION_FEATURE_GROUPS } from './common/constants';

export default function Home() {
    let [navOption, setNavOption] = useState(NAV_OPTION_MODELS);

    function renderCurrentComponent() {
        switch(navOption) {
            case NAV_OPTION_FEATURES:
                return <FeatureContainerComponent/>
            case NAV_OPTION_FEATURE_GROUPS:
                return <FeatureGroupContainerComponent/>
            default:
                return <ModelContainerComponent/>
        }
    }
    return <RootContainerLayout navOption={navOption} setNavOption={setNavOption}>
        {renderCurrentComponent()}
    </RootContainerLayout>
}