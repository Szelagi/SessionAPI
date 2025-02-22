import {defineConfig} from 'vitepress'

// https://vitepress.vuejs.org/config/app-configs
export default defineConfig({
    head: [
        ['link', {rel: 'icon', type: 'image/svg+xml', href: '/logo.svg'}],
        ['meta', {name: 'keywords', content: 'SessionAPI, Minecraft, game, containerization, server, minigames'}],
        ['meta', {name: 'author', content: 'Kamil Szelągiewicz'}],
        ['meta', {property: 'og:title', content: 'SessionAPI Documentation'}],
        ['meta', {property: 'og:description', content: 'Library for game containerization on Minecraft servers.'}],
        ['meta', {property: 'og:image', content: '/img/logo.svg'}]
    ],
    title: 'SessionAPI',
    description: 'The library facilitates game containerization on a Minecraft server, enabling the creation of isolated environments within the game with separate logic and state.',
    outDir: "../../docs",
    base: "/SessionAPI/",

    locales: {
        root: {
            label: 'Polski',
            lang: 'pl',
        },
        en: {
            label: "English",
            lang: 'en',
        }
    },

    themeConfig: {
        logo: '/logo.svg',
        nav: [
            {text: 'Community', link: 'https://discord.gg/za2pYfGWRN'},
            {text: 'GitHub', link: 'https://github.com/Szelagi/SessionAPI'},
        ],

        socialLinks: [
            {icon: 'discord', link: 'https://discord.gg/za2pYfGWRN'},
            {icon: 'linkedin', link: 'https://www.linkedin.com/in/kamil-szelagiewicz/'},
            {icon: 'github', link: 'https://github.com/Szelagi/SessionAPI'}
        ],

        sidebar: {
            pl: [
                {
                    text: 'Podstawy',
                    items: [
                        {text: 'Wprowadzenie', link: '/pl/'},
                        {text: 'Instalacja', link: '/pl/installation'},
                        {text: 'Pierwszy projekt', link: '/pl/first-project'},
                        // {text: 'Kreator mapy', link: '/creator'},
                    ]
                },
                {
                    text: 'Dokumentacja',
                    items: [
                        // {text: 'Kreator mapy', link: '/'},
                        // {text: 'Adnotacje przestrzenne', link: '/getting-started/faq'},
                        // {text: 'Sesja (Session)', link: '/getting-started/installation'},
                        // {text: 'Mapa (Board)', link: '/getting-started/quick-start'},
                        // {text: 'Kontroller (Controller)', link: '/getting-started/faq'},
                        // {text: 'Zagnieżdzenia', link: '/getting-started/faq'},
                        // {text: 'Listenery', link: '/getting-started/faq'},
                        // {text: 'Zadania i wątki', link: '/getting-started/faq'},

                    ]
                },
                {
                    text: 'Wbudowane komponenty',
                    items: []
                },
                {
                    text: 'Artykuły',
                    items: [
                        {text: 'Migracja do 2.3', link: '/pl/migration_2.3'},
                        {text: 'Bezpieczeństwo w grach', link: '/pl/security'}
                    ]
                },
            ],
            en: [
                {
                    text: 'Basics',
                    items: [
                        {text: 'Introduction', link: '/en/'},
                    ]
                },
                {
                    text: 'Articles',
                    items: [
                        {text: 'Migration to 2.3', link: '/en/migration_2.3'},
                    ]
                },
            ]
        },

        localeLinks: {
            text: 'Language',
            items: [
                { text: 'Polski', link: '/pl/' },
                { text: 'English', link: '/en/' }
            ]
        }
    },

});
