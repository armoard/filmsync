const loginDialog = document.getElementById('login-dialog');
const registerDialog = document.getElementById('register-dialog');
const openLoginButton = document.getElementById('login');
const createAccountButton = document.getElementById('create-account');

let registeredEmail = "";
function showDialogWithAnimation(dialog) {
    dialog.classList.add('dialog-animate');
    dialog.showModal();

    dialog.addEventListener('close', () => {
        dialog.classList.remove('dialog-animate');
    });
}


openLoginButton.addEventListener('click', () => {
    showDialogWithAnimation(loginDialog);
});


createAccountButton.addEventListener('click', () => {
    showDialogWithAnimation(registerDialog);
});

function handleRegister() {
    const username = document.getElementById('register-username').value;
    const email = document.getElementById('register-email').value;
    const password = document.getElementById('register-password').value;

    const usernameError = document.getElementById('username-error');
    const emailError = document.getElementById('email-error');
    const passwordError = document.getElementById('password-error');
    const loadingSpinner = document.getElementById('register-spinner');

    resetErrorMessages(usernameError, emailError, passwordError);

    const hasError = validateRegisterFields(username, email, password, usernameError, emailError, passwordError);

    if (hasError) return;

    registeredEmail = email;
    showLoadingSpinner(loadingSpinner);
    sendRegisterRequest(username, email, password, usernameError, emailError, loadingSpinner);
}


function resetErrorMessages(usernameError, emailError, passwordError) {
    usernameError.style.display = "none";
    emailError.style.display = "none";
    passwordError.style.display = "none";
}


function validateRegisterFields(username, email, password, usernameError, emailError, passwordError) {
    let hasError = false;

    if (!username) {
        usernameError.textContent = "Username is required";
        usernameError.style.display = "block";
        hasError = true;
    }

    if (!email) {
        emailError.textContent = "Email is required";
        emailError.style.display = "block";
        hasError = true;
    }


    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (email && !emailPattern.test(email)) {
        emailError.textContent = "Please enter a valid email address";
        emailError.style.display = "block";
        hasError = true;
    }

    if (!password) {
        passwordError.textContent = "Password is required";
        passwordError.style.display = "block";
        hasError = true;
    }


    if (password && password.length < 6) {
        passwordError.textContent = "Password must be at least 6 characters long";
        passwordError.style.display = "block";
        hasError = true;
    }

    return hasError;
}

function showLoadingSpinner(loadingSpinner) {
    loadingSpinner.style.display = "flex";
}

function hideLoadingSpinner(loadingSpinner) {
    loadingSpinner.style.display = "none";
}


function sendRegisterRequest(username, email, password, usernameError, emailError, loadingSpinner) {
    fetch('/auth/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, email, password })
    })
    .then(response => {
        if (response.ok) {
            handleSuccess(loadingSpinner);
        } else {
            return response.json().then(errorResponse => {
                handleErrorResponse(response.status, errorResponse, usernameError, emailError);
            });
        }
    })
    .catch(error => {
        console.error('Error:', error);
    })
    .finally(() => {
        hideLoadingSpinner(loadingSpinner);
    });
}

function handleSuccess(loadingSpinner) {
    const registerDialog = document.getElementById('register-dialog');
    const verificationDialog = document.getElementById('verification-dialog');


    registerDialog.classList.add('dialog-animate-out');
    setTimeout(() => {
        registerDialog.close();
        registerDialog.classList.remove('dialog-animate-out');

        verificationDialog.showModal();
        verificationDialog.classList.add('dialog-animate-in');
    }, 500);
}


function handleErrorResponse(status, errorResponse, usernameError, emailError) {
    console.error(`Error ${status}:`, errorResponse.message);

    if (status === 409) {
        if (errorResponse.message.includes("Username")) {
            usernameError.textContent = errorResponse.message;
            usernameError.style.display = "block";
        } else if (errorResponse.message.includes("Email")) {
            emailError.textContent = errorResponse.message;
            emailError.style.display = "block";
        }
    } else {
        alert(`Error: ${errorResponse.message}`);
    }
}


function handleVerification() {
    const code = document.getElementById('verification-code').value;
    const codeError = document.getElementById('verification-error');
    const loginDialog = document.getElementById('login-dialog');
    const verificationDialog = document.getElementById('verification-dialog');
    const loadingSpinner = document.getElementById('verification-spinner');

    codeError.style.display = "none";

    if (!code) {
        codeError.textContent = "Please enter a valid code";
        codeError.style.display = "block";
        return;
    }

    showLoadingSpinner(loadingSpinner);

    fetch('/auth/verify', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: registeredEmail, verificationCode: code })
    })
    .then(response => {
        if (response.ok) {
            verificationDialog.classList.add('dialog-animate-out');
            setTimeout(() => {
                verificationDialog.close();
                verificationDialog.classList.remove('dialog-animate-out');
                showDialogWithAnimation(loginDialog);
            }, 500);
        } else {
            codeError.textContent = "Invalid verification code";
            codeError.style.display = "block";
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function handleLogin() {
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;

    const emailError = document.getElementById('login-email-error');
    const passwordError = document.getElementById('login-password-error');
    const loadingSpinner = document.getElementById('login-spinner');

    resetLoginErrors(emailError, passwordError);

    let hasError = validateLoginFields(email, password, emailError, passwordError);
    if (hasError) return;

    showLoadingSpinner(loadingSpinner);

    fetch('/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
    })
    .then(response => {
        if (response.ok) {
            window.location.href = '/home';
        } else {
            return response.json().then(errorResponse => {
                handleLoginErrorResponse(response.status, errorResponse, emailError, passwordError);
            });
        }
    })
    .catch(error => {
        console.error("Error during login:", error);
    })
    .finally(() => {
        hideLoadingSpinner(loadingSpinner);
    });
}

function handleLoginErrorResponse(status, errorResponse, emailError, passwordError) {
    console.error(`Error ${status}:`, errorResponse.message);


    if (status === 404 && errorResponse.message === "User not found") {
        emailError.textContent = "Email not found.";
        emailError.style.display = "block";
        passwordError.style.display = "none";
    }

    else if (status === 401 && errorResponse.message === "Bad credentials") {
        passwordError.textContent = "Incorrect password.";
        passwordError.style.display = "block";
        emailError.style.display = "none";
    } else {
              alert(`Error: ${errorResponse.message}`);
    }
}

function resetLoginErrors(emailError, passwordError) {
    emailError.style.display = "none";
    passwordError.style.display = "none";
}

function validateLoginFields(email, password, emailError, passwordError) {
    let hasError = false;

    if (!email) {
        emailError.textContent = "Email is required";
        emailError.style.display = "block";
        hasError = true;
    }

    if (!password) {
        passwordError.textContent = "Password is required";
        passwordError.style.display = "block";
        hasError = true;
    }

    return hasError;
}