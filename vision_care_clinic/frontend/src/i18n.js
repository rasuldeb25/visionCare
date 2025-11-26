import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import HttpApi from 'i18next-http-backend';
import LanguageDetector from 'i18next-browser-languagedetector';

i18n
    .use(HttpApi) // Loads translations from /public/locales
    .use(LanguageDetector) // Detects user language
    .use(initReactI18next) // Passes i18n down to react-i18next
    .init({
        fallbackLng: 'en', // Use 'en' if detected language is not available
        debug: false, // Set to false in production
        detection: {
            order: ['queryString', 'cookie', 'localStorage', 'path', 'htmlTag'],
            caches: ['cookie'],
        },
        interpolation: {
            escapeValue: false, // React already safes from xss
        },
        backend: {
            // This path is relative to your `public` folder
            loadPath: '/locales/{{lng}}/translation.json',
        },
    });

export default i18n;
