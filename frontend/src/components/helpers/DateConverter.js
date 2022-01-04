const plMonths = [
    'Styczeń',
    'Luty',
    'Marzec',
    'Kwiecień',
    'Maj',
    'Czerwiec',
    'Lipiec',
    'Sierpień',
    'Wrzesień',
    'Październik',
    'Listopad',
    'Grudzień'
]

const plDays = [
    'Nie',
    'Pon',
    'Wto',
    'Śro',
    'Czw',
    'Pią',
    'Sob'
]


export const dateConverter = (date, fullDate) => {

    const parsedDate = new Date(date)
    const day = parsedDate.getDate()
    const year = parsedDate.getFullYear()
    let hours = parsedDate.getHours()
    let minutes = parsedDate.getMinutes()
    let seconds = parsedDate.getSeconds()

    if (hours < 10) hours = `0${hours}`
    if (minutes < 10) minutes = `0${minutes}`
    if (seconds < 10) seconds = `0${seconds}`

    const time = `${hours}:${minutes}:${seconds}`
    const month = plMonths[parsedDate.getMonth()]
    const weekDay = plDays[parsedDate.getDay()]
    if (fullDate) {
        return `${weekDay}, ${day} ${month} ${year} ${time}`
    } else {
        return `${weekDay}, ${day} ${month} ${year}`
    }
}