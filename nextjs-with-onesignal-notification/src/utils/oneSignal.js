export default async function initOneSignal(externalUserId) {
    if (typeof window === 'undefined') {
        return;
    }

    try {
        const OneSignal = (await import('react-onesignal')).default;

        await OneSignal.init({
            appId: process.env.NEXT_PUBLIC_ONESIGNAL_APP_ID,
            serviceWorkerPath: '/OneSignalSDKWorker.js',
            notifyButton: {
                enable: true,
            },
            allowLocalhostAsSecureOrigin: true,
        });

        await OneSignal.Notifications.requestPermission();

        if (externalUserId) {
            // Use login for targeted user notifications via include_aliases.external_id.
            await OneSignal.login(externalUserId);
        }

        console.log('[OneSignal] initialized', {
            externalUserId: externalUserId || null,
            permission: OneSignal.Notifications.permission,
            onesignalId: OneSignal.User?.onesignalId || null,
            subscriptionId: OneSignal.User?.PushSubscription?.id || null,
            optedIn: OneSignal.User?.PushSubscription?.optedIn || false,
        });
    } catch (error) {
        console.error('[OneSignal] initialization failed', error);
    }
}