
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

    function addSet() {
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

    addSet();
}
