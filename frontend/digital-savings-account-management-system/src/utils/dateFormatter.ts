// @author Khamitkar Sai Chandan

export function formatDate(dateStr: string) {
    const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' }
    return new Date(dateStr).toLocaleDateString(undefined, options)
}

export function formatDateShort(dateStr: string) {
    const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'short', day: '2-digit' }
    return new Date(dateStr).toLocaleDateString(undefined, options)
}