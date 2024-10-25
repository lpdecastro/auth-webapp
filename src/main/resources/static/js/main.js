document.addEventListener('DOMContentLoaded', function () {
    const container = document.getElementById('professional-info-container');

    // Remove button handler
    container.addEventListener('click', function (event) {
        if (event.target.classList.contains('remove-btn')) {
            const item = event.target.closest('.professional-item');
            item.remove(); // Remove the entire div for the professional info
            updateIndexes();
        }
    });

    // Update the indexes after removal to maintain proper bindings
    function updateIndexes() {
        const items = container.querySelectorAll('.professional-item');
        items.forEach((item, index) => {
            item.querySelectorAll('input').forEach(input => {
                input.name = input.name.replace(/\[\d+\]/, `[${index}]`);
            });
        });
    }

    // Add new professional info dynamically
    const addBtn = document.getElementById('add-professional-info-btn');
    addBtn.addEventListener('click', function () {
        const newIndex = container.querySelectorAll('.professional-item').length;
        const newEntry = `
                <div class="professional-item" data-index="${newIndex}">
                    <div class="mb-3">
                        <label class="form-label">Job Title</label>
                        <input type="text" name="professionalInfos[${newIndex}].title" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Company</label>
                        <input type="text" name="professionalInfos[${newIndex}].company" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Industry</label>
                        <input type="text" name="professionalInfos[${newIndex}].industry" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Years of Experience</label>
                        <input type="number" name="professionalInfos[${newIndex}].yearsOfExperience" class="form-control" min="0">
                    </div>
                    <button type="button" class="btn btn-danger remove-btn">Remove</button>
                    <hr>
                </div>
            `;
        container.insertAdjacentHTML('beforeend', newEntry);
    });
});

// Toggle password visibility for current, new, and confirm password fields
document.getElementById('toggle-current-password').addEventListener('click', function () {
    const currentPasswordField = document.getElementById('currentPassword');
    const currentEyeIcon = document.getElementById('current-eye-icon');
    const isPasswordHidden = currentPasswordField.getAttribute('type') === 'password';
    currentPasswordField.setAttribute('type', isPasswordHidden ? 'text' : 'password');
    currentEyeIcon.classList.toggle('fa-eye');
    currentEyeIcon.classList.toggle('fa-eye-slash');
});

document.getElementById('toggle-new-password').addEventListener('click', function () {
    const newPasswordField = document.getElementById('newPassword');
    const newEyeIcon = document.getElementById('new-eye-icon');
    const isPasswordHidden = newPasswordField.getAttribute('type') === 'password';
    newPasswordField.setAttribute('type', isPasswordHidden ? 'text' : 'password');
    newEyeIcon.classList.toggle('fa-eye');
    newEyeIcon.classList.toggle('fa-eye-slash');
});

document.getElementById('toggle-confirm-password').addEventListener('click', function () {
    const confirmPasswordField = document.getElementById('confirmPassword');
    const confirmEyeIcon = document.getElementById('confirm-eye-icon');
    const isPasswordHidden = confirmPasswordField.getAttribute('type') === 'password';
    confirmPasswordField.setAttribute('type', isPasswordHidden ? 'text' : 'password');
    confirmEyeIcon.classList.toggle('fa-eye');
    confirmEyeIcon.classList.toggle('fa-eye-slash');
});

// Password Match Validation
document.getElementById('changePasswordForm').addEventListener('submit', function (e) {
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (newPassword !== confirmPassword) {
        e.preventDefault(); // Prevent form submission

        // Check if error alert already exists
        let errorAlert = document.querySelector('.alert-danger');
        if (!errorAlert) {
            // Create error alert dynamically if it doesn't exist
            errorAlert = document.createElement('div');
            errorAlert.classList.add('alert', 'alert-danger', 'alert-dismissible', 'fade', 'show');
            errorAlert.setAttribute('role', 'alert');

            // Add message span inside alert
            const errorMessageSpan = document.createElement('span');
            errorMessageSpan.textContent = 'Passwords do not match';
            errorAlert.appendChild(errorMessageSpan);

            // Add close button
            const closeButton = document.createElement('button');
            closeButton.classList.add('btn-close');
            closeButton.setAttribute('type', 'button');
            closeButton.setAttribute('data-bs-dismiss', 'alert');
            closeButton.setAttribute('aria-label', 'Close');
            errorAlert.appendChild(closeButton);

            // Insert the error alert into the DOM (before the form)
            const form = document.getElementById('changePasswordForm');
            form.parentNode.insertBefore(errorAlert, form);
        } else {
            // Update existing error message if alert already exists
            errorAlert.querySelector('span').textContent = 'Passwords do not match';
        }
    }
});