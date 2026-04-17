'use client';

import { useEffect } from 'react';
import initOneSignal from '../utils/oneSignal';

export default function OneSignalInitializer() {
    useEffect(() => {
        // Must match your backend user identity when sending targeted pushes.
        const userId = process.env.NEXT_PUBLIC_TEST_EXTERNAL_USER_ID || '39095bb2-a078-4264-b95b-3511d00f641f';
        initOneSignal(userId);
    }, []);

    return null;
}