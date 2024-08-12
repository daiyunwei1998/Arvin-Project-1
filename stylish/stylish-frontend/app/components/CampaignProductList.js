"use client";
import React, { useEffect, useState } from 'react';
import { Table, Spin, Input, Upload, Button, message, DatePicker } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import moment from 'moment';

export default function CampaignProductList() {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedRowKeys, setSelectedRowKeys] = useState([]);

    useEffect(() => {
        const fetchAllData = async () => {
            let allData = [];
            let nextPaging = null;

            try {
                do {
                    const response = await fetch(`https://stylish.yunweidai.net/api/1.0/products/all${nextPaging ? `?paging=${nextPaging}` : ''}`);
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    const result = await response.json();
                    allData = [...allData, ...result.data];
                    nextPaging = result.next_paging;
                } while (nextPaging);

                setData(allData);
            } catch (error) {
                console.error('Error fetching data:', error);
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchAllData();
    }, []);

    const handleStoryChange = (value, index) => {
        const newData = [...data];
        newData[index].story = value; // Update story in the local state
        setData(newData);
    };

    const handleContentChange = (value, index) => {
        const newData = [...data];
        newData[index].content = value; // Update story in the local state
        setData(newData);
    };

    const handleReleaseDateChange = (date, index) => {
        const newData = [...data];
        newData[index].release_date = date ? date.format('YYYY-MM-DD') : null; // Format date as 'YYYY-MM-DD'
        setData(newData);
    };

    const handleCloseDateChange = (date, index) => {
        const newData = [...data];
        newData[index].close_date = date ? date.format('YYYY-MM-DD') : null; // Format date as 'YYYY-MM-DD'
        setData(newData);
    };

    const handleImageUpload = async (file, id) => {
        const formData = new FormData();
        formData.append('image', file);

        try {
            const response = await fetch(`https://stylish.yunweidai.net/api/1.0/products/${id}/upload-image`, {
                method: 'POST',
                body: formData,
            });

            if (!response.ok) {
                throw new Error('Image upload failed');
            }

            const updatedData = await response.json();
            console.log(updatedData);
            const newData = data.map(item => item.id === id ? { ...item, main_image: updatedData.url } : item);
            setData(newData);
            message.success('Image uploaded successfully');
        } catch (error) {
            console.error('Error uploading image:', error);
            message.error('Image upload failed');
        }
    };

    const handleSubmit = async () => {
        try {
            const selectedData = data.filter(item => selectedRowKeys.includes(item.id));
            const renamedData = selectedData.map(item => {
                // Create a new object with 'picture' instead of 'main_image' => make backend development more comfortable
                return {
                    ...item,
                    product_id: item.id,
                    picture: item.main_image  // Rename 'main_image' to 'picture'
                };
            });
            const response = await fetch('https://stylish.yunweidai.net/api/1.0/marketing/campaigns', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(renamedData),
            });
            
            if (!response.ok) {
                throw new Error('Submit failed');
            }

            message.success('Data submitted successfully');
        } catch (error) {
            console.error('Error submitting data:', error);
            message.error('Submit failed');
        }
    };


    const onSelectChange = (newSelectedRowKeys) => {
        console.log('selectedRowKeys changed: ', newSelectedRowKeys);
        setSelectedRowKeys(newSelectedRowKeys);
    };

    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
    };

    if (loading) return <Spin style={{ display: 'block', margin: '20px auto' }} />;
    if (error) return <div>Error: {error}</div>;
    const columns = [
        {
            title: 'Title',
            dataIndex: 'title',
        },
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: 'Story',
            dataIndex: 'story',
            render: (text, record, index) => (
                <Input
                    value={text}
                    onChange={(e) => handleStoryChange(e.target.value, index)}
                />
            ),
        },
        {
            title: 'Content',
            dataIndex: 'content',
            render: (text, record, index) => (
                <Input
                value={text}
                onChange={(e) => handleContentChange(e.target.value, index)}
            />
            ),
        },
        {
            title: 'Release',
            dataIndex: 'release_date',
            render: (text, record, index) => (
                <DatePicker
                    value={text ? moment(text, 'YYYY-MM-DD') : null}
                    onChange={(date) => handleReleaseDateChange(date, index)}
                />
            ),
        },
        {
            title: 'Close',
            dataIndex: 'close_date',
            render: (text, record, index) => (
                <DatePicker
                    value={text ? moment(text, 'YYYY-MM-DD') : null}
                    onChange={(date) => handleCloseDateChange(date, index)}
                />
            ),
        },
        {
            title: 'Picture',
            dataIndex: 'main_image',
            render: (text, record) => (
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                    <img
                        src={text}
                        alt="Preview"
                        style={{ width: 50, height: 50, objectFit: 'cover' }}
                    />
                    <Upload
                        showUploadList={false}
                        beforeUpload={(file) => handleImageUpload(file, record.id)}
                    >
                        <Button icon={<UploadOutlined />}>Change</Button>
                    </Upload>
                </div>
            ),
        },
    ];
    return (
        <>
        <Table rowSelection={rowSelection} columns={columns} dataSource={data} rowKey="id" pagination={false} />
        <div style={{ marginBottom: '10px', marginTop: '10px', textAlign:'center' }}>
            <Button type="primary" onClick={handleSubmit}>Submit Selected Data</Button>
        </div>
    </>
    );
}