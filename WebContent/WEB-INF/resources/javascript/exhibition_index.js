function initialiseExhibitionSetup() {
    const form = document.getElementById('exhibitionSetupForm');
    const setsContainer = document.getElementById('setsContainer');
    const setTemplate = document.getElementById('setTemplate');
    const addSetBtn = document.getElementById('addSetBtn');
    if (!form || !setsContainer || !setTemplate || !addSetBtn) return;

    function updateSetNames() {
        const sets = setsContainer.querySelectorAll('.set-card');
        sets.forEach((card, index) => {
            card.querySelector('.set-heading').textContent = 'Set ' + (index + 1);
            card.querySelector('.set-name').name = 'sets[' + index + '].setName';
            card.querySelector('.set-type').name = 'sets[' + index + '].setType';
            card.querySelector('.match-style').name = 'sets[' + index + '].matchStyle';
            card.querySelector('.player1').name = 'sets[' + index + '].group1Player1';
            card.querySelector('.player1b').name = 'sets[' + index + '].group1Player2';
            card.querySelector('.player2').name = 'sets[' + index + '].group2Player1';
            card.querySelector('.player2b').name = 'sets[' + index + '].group2Player2';
        });
        setsContainer.querySelectorAll('.remove-set-btn').forEach(button => {
            button.disabled = sets.length === 1;
        });
    }

    function addSet(savedSet = null) {
        const fragment = setTemplate.content.cloneNode(true);
        const card = fragment.querySelector('.set-card');
        const style = card.querySelector('.match-style');
        const secondPlayers = [card.querySelector('.player1b'), card.querySelector('.player2b')];

        style.addEventListener('change', () => {
            const doubles = style.value === 'DOUBLES';
            secondPlayers.forEach(select => {
                select.style.display = doubles ? '' : 'none';
                select.required = doubles;
                if (!doubles) select.value = '';
            });
        });

        card.querySelector('.remove-set-btn').addEventListener('click', () => {
            card.remove();
            updateSetNames();
        });

        if (savedSet) {
            card.querySelector('.set-name').value = savedSet.setName;
            card.querySelector('.set-type').value = savedSet.setType;
            style.value = savedSet.matchStyle;
            const doubles = style.value === 'DOUBLES';
            secondPlayers.forEach(select => {
                select.style.display = doubles ? '' : 'none';
                select.required = doubles;
            });
            card.querySelector('.player1').value = savedSet.group1Player1;
            card.querySelector('.player1b').value = savedSet.group1Player2;
            card.querySelector('.player2').value = savedSet.group2Player1;
            card.querySelector('.player2b').value = savedSet.group2Player2;
        }

        setsContainer.appendChild(fragment);
        updateSetNames();
    }

    addSetBtn.addEventListener('click', addSet);

    form.addEventListener('submit', event => {
        const cards = setsContainer.querySelectorAll('.set-card');

        if (cards.length === 0) {
            event.preventDefault();
            alert('Please add at least one set.');
            return;
        }

        for (const card of cards) {
            const p1 = card.querySelector('.player1').value;
            const p2 = card.querySelector('.player2').value;
            const doubles = card.querySelector('.match-style').value === 'DOUBLES';
            const p1b = card.querySelector('.player1b').value;
            const p2b = card.querySelector('.player2b').value;

            if (!p1 || !p2 || (doubles && (!p1b || !p2b))) {
                event.preventDefault();
                alert('Please select all players for each set.');
                return;
            }

            if (p1 === p2 || (doubles && (p1 === p1b || p1 === p2b || p1b === p2 || p1b === p2b || p2 === p2b))) {
                event.preventDefault();
                alert('Please select different players within each set.');
                return;
            }
        }

        const group1 = form.querySelector('[name="group1Name"]').value.trim();
        const group2 = form.querySelector('[name="group2Name"]').value.trim();

        if (!group1 || !group2 || group1.toLowerCase() === group2.toLowerCase()) {
            event.preventDefault();
            alert('Please enter two different group names.');
        }
    });

    const savedSetElements = Array.from(document.querySelectorAll('#savedSetsData .saved-set'));
    if (savedSetElements.length > 0) {
        savedSetElements.forEach(element => addSet({
            setName: element.getAttribute('data-set-name') || '',
            setType: (element.getAttribute('data-set-type') || 'REGULAR').toUpperCase(),
            matchStyle: (element.getAttribute('data-match-style') || 'SINGLES').toUpperCase(),
            group1Player1: element.getAttribute('data-group1-player1') || '',
            group1Player2: element.getAttribute('data-group1-player2') || '',
            group2Player1: element.getAttribute('data-group2-player1') || '',
            group2Player2: element.getAttribute('data-group2-player2') || ''
        }));
    } else {
        addSet();
    }
}

document.addEventListener('DOMContentLoaded', initialiseExhibitionSetup);

function showOverallMatchResult() {
    const section = document.getElementById('overallMatchResult');
    const finishButton = document.getElementById('finishMatchBtn');
    if (!section) return;
    section.style.display = '';
    section.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    if (finishButton) finishButton.style.display = 'none';
}

document.addEventListener('DOMContentLoaded', () => {
    const finishButton = document.getElementById('finishMatchBtn');
    if (finishButton) finishButton.addEventListener('click', showOverallMatchResult);
    document.querySelectorAll('form[action]').forEach(form => {
        const action = form.getAttribute('action') || '';
        let message = '';
        if (action.includes('/exhibition/logger/set/complete')) message = 'Complete this set and record the selected winner?';
        else if (action.includes('/exhibition/logger/set/reopen')) message = 'Reopen this set? The scores will be preserved and the recorded winner cleared.';
        else if (action.includes('/exhibition/logger/match/complete')) message = 'Declare the selected group as the overall match winner?';
        if (message) form.addEventListener('submit', event => {
            if (!window.confirm(message)) event.preventDefault();
        });
    });
});


document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.score-display').forEach(display => {
        const group = display.getAttribute('data-score-group');
        const editForm = document.querySelector('.score-edit-form[data-score-group="' + group + '"]');
        const editButton = display.querySelector('.score-edit-btn');
        const input = editForm ? editForm.querySelector('.score-edit-input') : null;
        const cancelButton = editForm ? editForm.querySelector('.score-cancel-btn') : null;
        if (!editForm || !editButton || !input || !cancelButton) return;
        editButton.addEventListener('click', () => {
            display.classList.add('d-none');
            editForm.classList.remove('d-none');
            input.focus();
            input.select();
        });
        cancelButton.addEventListener('click', () => {
            input.value = display.querySelector('.score-value').textContent.trim();
            editForm.classList.add('d-none');
            display.classList.remove('d-none');
        });
        editForm.addEventListener('submit', event => {
            const value = input.value.trim();
            if (!/^\d+$/.test(value) || Number(value) < 0 || Number(value) > 2147483647) {
                event.preventDefault();
                alert('Enter a whole number between 0 and 2147483647.');
            }
        });
    });
});
