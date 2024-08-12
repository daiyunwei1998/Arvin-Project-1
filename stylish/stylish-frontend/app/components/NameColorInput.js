// components/NameColorInput.js
import React, { useState } from 'react';
import { Button, Modal, Input, ColorPicker } from 'antd';

export default function NameColorInput({ onAdd }) {
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [name, setName] = useState('');
    const [code, setCode] = useState('#000000');

    const showModal = () => {
        setIsModalVisible(true);
    };

    const handleOk = () => {
        if (name && code) {
            onAdd({ name, code });
            setIsModalVisible(false);
            setName('');
            setCode('#000000');
        }
    };

    const handleCancel = () => {
        setIsModalVisible(false);
        setName('');
        setCode('#000000');
    };

    return (
        <>
            <Button type="primary" onClick={showModal}>
                Add Name-Color
            </Button>
            <Modal
                title="Add Name and Color"
                visible={isModalVisible}
                onOk={handleOk}
                onCancel={handleCancel}
            >
                <Input
                    placeholder="Enter name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    style={{ marginBottom: '10px' }}
                />
                <ColorPicker
                    value={code}
                    onChange={(code) => setCode(code.toHexString())}
                />
            </Modal>
        </>
    );
}